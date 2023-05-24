use proc_macro::TokenStream;
use quote::{quote, format_ident};
use syn::TraitBound;



pub fn make_clonable(mut trait_item:syn::ItemTrait) -> TokenStream{
    let trait_ident = &trait_item.ident;
    let clone_trait_ident = format_ident!("{}DynCloneAutoDerive", trait_ident);
    let stream : TokenStream = quote!(#clone_trait_ident).into();
    let new_bound : TraitBound = syn::parse(stream).unwrap();
    trait_item.supertraits.push(syn::TypeParamBound::Trait(new_bound));

    quote!{
        #trait_item
        pub trait #clone_trait_ident {
            fn clone_dyn(&self) -> Box<dyn #trait_ident>;
        }
    }.into()
}
