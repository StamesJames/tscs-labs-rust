use crate::bx;
use any_eq_derive::{AnyEq, AsAny, PartialEqAnyEq};

use super::{binary_expressions::BinExpr, AnyEq, AsAny, AsExpression, Expression};
use std::any::Any;

#[derive(Debug, AsAny, AnyEq, PartialEqAnyEq)]
pub enum NumExpr {
    Succ(Box<dyn Expression>),
    Pred(Box<dyn Expression>),
    IsZero(Box<dyn Expression>),
    Zero,
}

impl Expression for NumExpr {
    fn evaluate(&self) -> Option<Box<dyn Expression>> {
        match self {
            NumExpr::Succ(x) => x.evaluate().map(|x| NumExpr::Succ(x).as_boxed_expr()),
            NumExpr::Pred(x) => x.evaluate().map(|x| NumExpr::Pred(x).as_boxed_expr()),
            NumExpr::IsZero(x) => x.evaluate().map(|x| {
                if x == NumExpr::Zero.as_boxed_expr() {
                    BinExpr::True.as_boxed_expr()
                } else {
                    BinExpr::False.as_boxed_expr()
                }
            }),
            NumExpr::Zero => None,
        }
    }

    fn is_value(&self) -> bool {
        match self {
            NumExpr::Zero => true,
            NumExpr::Succ(x) => {
                x.as_any().downcast_ref::<NumExpr>().map_or(false, |x| x.is_value())
            },
            _ => false
        }
    }
}
