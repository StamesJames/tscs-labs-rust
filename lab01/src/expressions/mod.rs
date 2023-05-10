use std::{any::Any, fmt::Debug};

pub mod binary_expressions;

pub trait Expression: AsAny + Debug + ExpEq {
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
pub trait ExpEq {
    fn exp_eq(&self, other: &dyn Expression) -> bool;
}