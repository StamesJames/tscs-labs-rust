use proc_macro::{TokenStream};
use quote::{quote, format_ident};
use syn::{self, FieldsNamed, FieldsUnnamed, Type::TraitObject, punctuated::Punctuated, Variant, token::Comma};

#[proc_macro_derive(PartialEqAnyEq)]
pub fn hello_macro_derive(input: TokenStream) -> TokenStream {
    let ast = syn::parse(input).unwrap();

    impl_any_eq(&ast)
}

fn impl_any_eq(ast: &syn::DeriveInput) -> TokenStream {
    let name = &ast.ident;
    let data = &ast.data;
    let gen_field_comps = match data {
        syn::Data::Struct(data) => {
            match &data.fields {
                syn::Fields::Named(fields) => gen_part_eq_named_fields(fields),
                syn::Fields::Unnamed(fields) => gen_part_eq_unnamed_fields(fields),
                syn::Fields::Unit => quote!{true},
            }
        },
        syn::Data::Enum(data) => {
            gen_part_eq_enum_variants(&data.variants)
        },
        syn::Data::Union(_) => todo!(),
    };
    let gen_part_eq = quote!{
        impl PartialEq for #name {
            fn eq(&self, other: &Self) -> bool {
                #gen_field_comps
            }
        }
    };
    gen_part_eq.into()
}
fn gen_part_eq_named_fields(fields: &FieldsNamed) -> quote::__private::TokenStream {
    let mut gen_fields_eq = quote!(true);
    for field in &fields.named {
        let field_name = &field.ident;
        let field_name = field_name.as_ref().unwrap();
        match &field.ty {
            TraitObject(_) =>
            {
                gen_fields_eq =
                    quote!{
                        #gen_fields_eq &&
                        self.#field_name.any_eq(other.#field_name.as_any())
                    }
            },
            _ => {
                gen_fields_eq = 
                    quote!{
                        #gen_fields_eq &&
                        self.#field_name == other.field_name
                    }
            }
        }
    }
    return gen_fields_eq;
}
fn gen_part_eq_unnamed_fields(fields: &FieldsUnnamed) -> quote::__private::TokenStream {
    quote!(true)
}

fn gen_part_eq_enum_variants(varaints: &Punctuated<Variant, Comma>) -> quote::__private::TokenStream{
    let mut gen_match_arms = quote!();
    for variant in varaints {
        let variant_name = &variant.ident;
        match &variant.fields {
            syn::Fields::Named(fields) => {
                let mut fields_named_left = quote!();
                let mut fields_named_right = quote!();
                let mut fields_named_comps = quote!(true);
                for field in &fields.named {
                    let field_name = field.ident.as_ref().unwrap();
                    let left_name = format_ident!("l_{}", field_name);
                    let right_name = format_ident!("r_{}", field_name);
                    fields_named_left = quote!{ #field_name : #left_name, #fields_named_left };
                    fields_named_right = quote!{ #field_name : #right_name, #fields_named_right };
                    match field.ty {
                        TraitObject(_) => fields_named_comps = quote!(#left_name.any_eq(#right_name.as_any())),
                        _ => fields_named_comps = quote!(#fields_named_comps && #left_name == #right_name)
                    }
                }
                gen_match_arms = quote!{#gen_match_arms , 
                     ( Self::#variant_name{fields_named_left}, Self::#variant_name{fields_named_right} ) => #fields_named_comps}
            }
            syn::Fields::Unnamed(fields) => {
                
            },
            syn::Fields::Unit => todo!(),
    }
    return quote!{
        match (self, other) {
            #gen_match_arms
            _ => false
        }
    };
}

enum Bluu{
    VA(i128),
    VB(i128),
    VNA{x:i128}
}
impl PartialEq for Bluu {
    fn eq(&self, other: &Self) -> bool {
        match (self, other) {
            (Self::VA(l0), Self::VA(r0)) => l0 == r0,
            (Self::VB(l0), Self::VB(r0)) => l0 == r0,
            (Self::VNA { x: l_x }, Self::VNA { x: r_x }) => l_x == r_x,
            _ => false,
        }
    }
}

struct Bla;
impl PartialEq for Bla {
    fn eq(&self, other: &Self) -> bool {
        true
    }
}
