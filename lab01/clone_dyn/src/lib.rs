use proc_macro::TokenStream;
use syn::{self, TraitItem};

/*
#[proc_macro_attribute]
pub fn clonable(attr: TokenStream, item: TokenStream) -> TokenStream {
    let item = syn::parse(item).unwrap();
    make_clonable(item)
}

fn make_clonable(item:syn::Item) -> TokenStream{
    match item {
        syn::Item::Trait(traitItem) => {

        },
        _ => panic!("expect TraitItem")
    }
}
    */