use proc_macro::TokenStream;
use quote::quote;
use syn;

#[proc_macro_derive(AnyEq)]
pub fn hello_macro_derive(input: TokenStream) -> TokenStream {
    let ast = syn::parse(input).unwrap();

    impl_any_eq(&ast)
}

fn impl_any_eq(ast: &syn::DeriveInput) -> TokenStream {
    let name = &ast.ident;
    let data = &ast.data;
    let gen_any_eq = quote!();
    //     let gen_part_eq = match data {
    //         syn::Data::Struct(data) => {
    //             match &data.fields {
    //                 syn::Fields::Named(fields) => gen_part_eq_named_fields(fields),
    //                 syn::Fields::Unnamed(fields) => gen_part_eq_unnamed_fields(fields),
    //                 syn::Fields::Unit => quote!{
    //                     impl PartialEq for #name {
    //                         fn eq(&self, other: &Self) -> bool {
    //                             true
    //                         }
    //                     }
    //                 }.into(),
    //             }
    //         },
    //         syn::Data::Enum(_) => todo!(),
    //         syn::Data::Union(_) => todo!(),
    //     };

    gen_any_eq.into()
}

// fn gen_part_eq_named_fields(fields: &FieldsNamed) -> TokenStream {
//     let mut gen_fields_eq = quote!(true);
//     for field in &fields.named {
//         match &field.ty {
//             TraitObject(_) =>
//             {
//                 gen_fields_eq =
//                     quote!{
//                         #gen_fields_eq &&
//                         self
//                     }
//             },
//             _ =>
//         }
//     }
//     return gen_fields_eq;
// }

// fn gen_part_eq_unnamed_fields(fields: &FieldsUnnamed) -> TokenStream {
//     todo!()
// }

// struct Bla;

// impl PartialEq for Bla {
//     fn eq(&self, other: &Self) -> bool {
//         true
//     }
// }
