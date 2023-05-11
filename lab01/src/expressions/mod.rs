use std::{any::Any, fmt::Debug};

pub mod binary_expressions;

pub trait Expression: AsAny + Debug + AnyEq {
    fn evaluate(self) -> Box<dyn Expression>;
}
pub trait AsAny: Any {
    fn as_any(&self) -> &dyn Any;
}
impl<T: Any> AsAny for T {
    fn as_any(&self) -> &dyn Any {
        self
    }
}
pub trait AnyEq {
    fn any_eq(&self, other: &dyn Any) -> bool;
}
impl<T> AnyEq for T 
where T: AsAny + PartialEq {
    fn any_eq(&self, other: &dyn Any) -> bool {
        (*self)
        .as_any()
        .downcast_ref::<Self>()
        .map_or(false, |lhs| {
            other
                .downcast_ref::<Self>()
                .map_or(false, |rhs| lhs == rhs)
        })
    }
}