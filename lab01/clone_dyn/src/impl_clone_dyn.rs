use proc_macro::TokenStream;
use quote::{format_ident, quote};
use syn::{
    self,
    punctuated::Punctuated,
    token::Comma,
    DataStruct, FieldsNamed, FieldsUnnamed, Ident, Path,
    Type::{self, TraitObject},
    TypePath, Variant,
};

pub fn impl_clone_dyn(ast: &syn::DeriveInput) -> TokenStream {
    let item_ident = &ast.ident;
    let item_data = &ast.data;
    let gen_field_clones = match item_data {
        syn::Data::Struct(data) => gen_clone_dyn_struct_fields(data),
        syn::Data::Enum(data) => gen_clone_dyn_enum_variants(&data.variants),
        syn::Data::Union(_) => panic!("Unions are not supported"),
    };
    let gen_dyn_clone = quote! {
        impl Clone for #item_ident {
            fn clone(&self) -> Self {
                #gen_field_clones
            }
        }
    };
    gen_dyn_clone.into()
}

fn gen_clone_dyn_struct_fields(data: &DataStruct) -> quote::__private::TokenStream {
    match &data.fields {
        syn::Fields::Named(fields) => gen_clone_dyn_struct_named_fields(fields),
        syn::Fields::Unnamed(fields) => gen_clone_dyn_struct_unnamed_fields(fields),
        syn::Fields::Unit => quote! { Self },
    }
}

fn gen_clone_dyn_struct_named_fields(fields: &FieldsNamed) -> quote::__private::TokenStream {
    let (fields_named, fields_named_clones) = make_named_fields(fields);
    quote!(match self {Self{#fields_named} => Self{#fields_named_clones} })
}
fn gen_clone_dyn_struct_unnamed_fields(fields: &FieldsUnnamed) -> quote::__private::TokenStream {
    let (fields_unnamed, fields_unnamed_clones) = make_unnamed_fields(fields);
    quote!(match self { Self(#fields_unnamed)  => Self(#fields_unnamed_clones) })
}

fn gen_clone_dyn_enum_variants(
    varaints: &Punctuated<Variant, Comma>,
) -> quote::__private::TokenStream {
    let mut gen_match_arms = quote!();
    for variant in varaints {
        let variant_name = &variant.ident;
        match &variant.fields {
            syn::Fields::Named(fields) => {
                let (fields_named, fields_named_clones) = make_named_fields(fields);
                gen_match_arms = quote! {
                    Self::#variant_name{#fields_named}=> Self::#variant_name{#fields_named_clones},
                    #gen_match_arms
                };
            }
            syn::Fields::Unnamed(fields) => {
                let (fields_unnamed, fields_unnamed_clones) = make_unnamed_fields(fields);
                gen_match_arms = quote! {
                    Self::#variant_name (#fields_unnamed) => Self::#variant_name (#fields_unnamed_clones) ,
                    #gen_match_arms
                };
            }
            syn::Fields::Unit => {
                gen_match_arms =
                    quote!(Self::#variant_name => Self::#variant_name, #gen_match_arms);
            }
        }
    }
    quote! {
        match self {
            #gen_match_arms
        }
    }
}

fn make_clone_for_type(field_name: &Ident, field_type: &Type) -> quote::__private::TokenStream {
    match field_type {
        TraitObject(_) => quote!(#field_name.clone_dyn()),
        Type::Reference(ty) => make_clone_for_type(field_name, &ty.elem),
        Type::Path(TypePath {
            path: Path { segments, .. },
            ..
        }) => {
            if segments.iter().any(|segment| match &segment.arguments {
                syn::PathArguments::AngleBracketed(args) => args
                    .args
                    .iter()
                    .any(|arg| matches!(arg, syn::GenericArgument::Type(TraitObject(_)))),
                _ => false,
            }) {
                quote!(#field_name.clone_dyn())
            } else {
                quote!(#field_name.clone())
            }
        }
        _ => quote!(#field_name.clone()),
    }
}

fn make_named_fields(
    fields: &FieldsNamed,
) -> (quote::__private::TokenStream, quote::__private::TokenStream) {
    let mut fields_named = quote!();
    let mut fields_named_clones = quote!();

    for field in &fields.named {
        let field_name = field.ident.as_ref().unwrap();
        fields_named = quote! { #field_name, #fields_named };

        let cloned_field = make_clone_for_type(field_name, &field.ty);
        fields_named_clones = quote!(#field_name : #cloned_field, #fields_named_clones);
    }
    (fields_named, fields_named_clones)
}

fn make_unnamed_fields(
    fields: &FieldsUnnamed,
) -> (quote::__private::TokenStream, quote::__private::TokenStream) {
    let mut fields_unnamed = quote!();
    let mut fields_unnamed_clones = quote!();
    for (n, field) in fields.unnamed.iter().enumerate().rev() {
        let field_name = format_ident!("this_{}", n);
        fields_unnamed = quote! { #field_name, #fields_unnamed };
        let field_clone = make_clone_for_type(&field_name, &field.ty);
        fields_unnamed_clones = quote!(#field_clone, #fields_unnamed_clones);
    }
    (fields_unnamed, fields_unnamed_clones)
}
