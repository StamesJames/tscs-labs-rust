use proc_macro::TokenStream;
mod any_eq;
mod as_any;
mod partial_eq_any_eq;

#[proc_macro_derive(AsAny)]
pub fn as_any_derive(input: TokenStream) -> TokenStream {
    let ast = syn::parse(input).unwrap();
    as_any::impl_as_any(&ast)
}
#[proc_macro_derive(AnyEq)]
pub fn any_eq_derive(input: TokenStream) -> TokenStream {
    let ast = syn::parse(input).unwrap();
    any_eq::impl_any_eq(&ast)
}

#[proc_macro_derive(PartialEqAnyEq)]
pub fn partial_eq_any_eq_derive(input: TokenStream) -> TokenStream {
    let ast = syn::parse(input).unwrap();
    partial_eq_any_eq::impl_partial_eq_any_eq(&ast)
}
