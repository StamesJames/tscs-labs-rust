use std::any::Any;

pub trait AsAny: Any {
    fn as_any(&self) -> &dyn Any;
}
pub trait AnyEq {
    fn any_eq(&self, other: &dyn Any) -> bool;
}
