use std::fmt::Debug;

use any_eq::{AnyEq, AsAny};

pub mod binary_expressions;
pub mod numeric_expressions;

pub trait Expression: AsAny + Debug + AnyEq + AsExpression {
    fn evaluate(&self) -> Option<Box<dyn Expression>>;
    fn evaluate_to_end(&self) -> Option<Box<dyn Expression>> {
        let mut ret =  self.evaluate();
        while let Some(x) = ret {
            ret = x.evaluate();
        }
        return ret;
    }
    fn progress_possible(&self) -> bool {
        self.evaluate().is_some()
    }
    fn is_value(&self) -> bool;
}


pub trait AsExpression {
    fn as_expr(&self) -> &dyn Expression;
    fn as_boxed_expr(self) -> Box<dyn Expression>;
}
impl<T: Expression> AsExpression for T {
    fn as_expr(&self) -> &dyn Expression {
        self
    }
    fn as_boxed_expr(self) -> Box<dyn Expression> {
        Box::new(self)
    }
}
impl PartialEq for dyn Expression {
    fn eq(&self, other: &Self) -> bool {
        self.any_eq(other.as_any())
    }
}

#[cfg(test)]
mod tests {
    use any_eq_derive::PartialEqAnyEq;

    use crate::bx;

    #[test]
    fn any_eq_enum_without_dyn() {
        #[derive(PartialEqAnyEq)]
        enum TE {
            TE1(i32, String, Box<String>),
            TE2 { x: i32, y: String, z: Box<String> },
        }
        assert!(
            TE::TE1(0, "abc".to_string(), bx!("xyz".to_string()))
                == TE::TE1(0, "abc".to_string(), bx!("xyz".to_string()))
        );
        assert!(
            TE::TE1(1, "abc".to_string(), bx!("xyz".to_string()))
                != TE::TE1(0, "abc".to_string(), bx!("xyz".to_string()))
        );
        assert!(
            TE::TE1(0, "ab".to_string(), bx!("xyz".to_string()))
                != TE::TE1(0, "abc".to_string(), bx!("xyz".to_string()))
        );
        assert!(
            TE::TE1(0, "abc".to_string(), bx!("xy".to_string()))
                != TE::TE1(0, "abc".to_string(), bx!("xyz".to_string()))
        );

        assert!(
            TE::TE2 {
                x: 0,
                y: "abc".to_string(),
                z: bx!("xyz".to_string())
            } == TE::TE2 {
                x: 0,
                y: "abc".to_string(),
                z: bx!("xyz".to_string())
            }
        );
        assert!(
            TE::TE2 {
                x: 1,
                y: "abc".to_string(),
                z: bx!("xyz".to_string())
            } != TE::TE2 {
                x: 0,
                y: "abc".to_string(),
                z: bx!("xyz".to_string())
            }
        );
        assert!(
            TE::TE2 {
                x: 0,
                y: "ab".to_string(),
                z: bx!("xyz".to_string())
            } != TE::TE2 {
                x: 0,
                y: "abc".to_string(),
                z: bx!("xyz".to_string())
            }
        );
        assert!(
            TE::TE2 {
                x: 0,
                y: "abc".to_string(),
                z: bx!("xy".to_string())
            } != TE::TE2 {
                x: 0,
                y: "abc".to_string(),
                z: bx!("xyz".to_string())
            }
        );
    }

    #[test]
    fn any_eq_named_struct_without_dyn() {
        #[derive(PartialEqAnyEq)]
        struct TS {
            x: i32,
            y: String,
            z: Box<String>,
        }
        assert!(
            TS {
                x: 0,
                y: "abc".to_string(),
                z: bx!("xyz".to_string())
            } == TS {
                x: 0,
                y: "abc".to_string(),
                z: bx!("xyz".to_string())
            }
        );
        assert!(
            TS {
                x: 1,
                y: "abc".to_string(),
                z: bx!("xyz".to_string())
            } != TS {
                x: 0,
                y: "abc".to_string(),
                z: bx!("xyz".to_string())
            }
        );
        assert!(
            TS {
                x: 0,
                y: "ab".to_string(),
                z: bx!("xyz".to_string())
            } != TS {
                x: 0,
                y: "abc".to_string(),
                z: bx!("xyz".to_string())
            }
        );
        assert!(
            TS {
                x: 0,
                y: "abc".to_string(),
                z: bx!("xy".to_string())
            } != TS {
                x: 0,
                y: "abc".to_string(),
                z: bx!("xyz".to_string())
            }
        );
    }

    #[test]
    fn any_eq_unnamed_struct_without_dyn() {
        #[derive(PartialEqAnyEq)]
        struct TS(i32, String, Box<String>);
        assert!(
            TS(0, "abc".to_string(), bx!("xyz".to_string()))
                == TS(0, "abc".to_string(), bx!("xyz".to_string()))
        );
        assert!(
            TS(1, "abc".to_string(), bx!("xyz".to_string()))
                != TS(0, "abc".to_string(), bx!("xyz".to_string()))
        );
        assert!(
            TS(0, "ab".to_string(), bx!("xyz".to_string()))
                != TS(0, "abc".to_string(), bx!("xyz".to_string()))
        );
        assert!(
            TS(0, "abc".to_string(), bx!("xy".to_string()))
                != TS(0, "abc".to_string(), bx!("xyz".to_string()))
        );
    }
}
