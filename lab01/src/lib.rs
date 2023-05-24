pub mod expressions;
pub mod parsers;

#[macro_export]
macro_rules! bx {
    ($e:expr) => {
        Box::new($e)
    };
}
