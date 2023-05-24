use proc_macro::TokenStream;
use syn::{self, parse::Parser, punctuated::Punctuated, token::Comma, Ident, Item};
mod impl_clone_dyn;
mod make_clonable;
use make_clonable::make_clonable;
use quote::{format_ident, quote};

#[proc_macro_derive(CloneDyn)]
pub fn clone_dyn_derive(input: TokenStream) -> TokenStream {
    let ast = syn::parse(input).unwrap();
    impl_clone_dyn::impl_clone_dyn(&ast)
}

#[proc_macro_attribute]
pub fn dyn_clonable(_attr: TokenStream, item: TokenStream) -> TokenStream {
    let item = syn::parse(item).unwrap();
    if let syn::Item::Trait(trait_item) = item {
        make_clonable(trait_item)
    } else {
        panic!("Attribute must be attached to a Trait")
    }
}

#[proc_macro_attribute]
pub fn dyn_clonable_for_traits(attr: TokenStream, item: TokenStream) -> TokenStream {
    let parser = Punctuated::<Ident, Comma>::parse_terminated;
    let args = parser.parse(attr).expect("wasn't able to parse attrs");
    let item: Item = syn::parse(item).expect("wasn't able to parse Item");
    let item_ident: &Ident = match &item {
        Item::Enum(i) => &i.ident,
        Item::Struct(i) => &i.ident,
        _ => panic!("item must be Enum or struct"),
    };
    let mut impls = quote!();
    for arg in args {
        let trait_ident = format_ident!("{}DynCloneAutoDerive", arg);
        impls = quote!(
            impl #trait_ident for #item_ident {
                fn clone_dyn(&self) -> Box<dyn #arg> {
                    Box::new(self.clone())
                }
            }

            #impls
        )
    }
    quote!(#item #impls).into()
}
