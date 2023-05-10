use proc_macro::TokenStream;
use quote::{quote, __private::ext::RepToTokensExt};
use syn;

#[proc_macro_derive(ExprEq)]
pub fn hello_macro_derive(input: TokenStream) -> TokenStream {
    // Construct a representation of Rust code as a syntax tree
    // that we can manipulate
    let ast = syn::parse(input).unwrap();

    // Build the trait implementation
    impl_expr_eq(&ast)
}

fn impl_expr_eq(ast: &syn::DeriveInput) -> TokenStream {
    let name = &ast.ident;
    let data = &ast.data;

    let gen_exp_eq = quote!{
        impl ExpEq for #name {
            fn exp_eq(&self, other: &dyn Expression) -> bool {
                (*self)
                    .as_any()
                    .downcast_ref::<#name>()
                    .map_or(false, |lhs| {
                        other
                            .as_any()
                            .downcast_ref::<#name>()
                            .map_or(false, |rhs| lhs == rhs)
                    })
            }
        }
        impl PartialEq for #name {

        }
    };

    let gen_part_eq = match data {
        syn::Data::Struct(data) => {
            match &data.fields {
                syn::Fields::Named(fields) => {
                    for field in &fields.named {
                        
                    }
                },
                syn::Fields::Unnamed(_) => todo!(),
                syn::Fields::Unit => todo!(),
            }
        },
        syn::Data::Enum(_) => todo!(),
        syn::Data::Union(_) => todo!(),
    };

    gen_exp_eq.into()
}