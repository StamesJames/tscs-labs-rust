use any_eq_derive::{ AnyEq, AsAny, PartialEqAnyEq };

use super::{
    binary_expressions::BinExpr,
    AnyEq,
    AsAny,
    AsExpression,
    CloneDynExpression,
    Expression,
};
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
            NumExpr::IsZero(x) => if x.progress_possible() {
                x.evaluate().map(|nx| NumExpr::IsZero(nx).as_boxed_expr())
            } else if x == &NumExpr::Zero.as_boxed_expr() {
                Some(BinExpr::True.as_boxed_expr())
            } else {
                Some(BinExpr::False.as_boxed_expr())
            }
            NumExpr::Zero => None,
        }
    }

    fn is_value(&self) -> bool {
        match self {
            NumExpr::Zero => true,
            NumExpr::Succ(x) =>
                x
                    .as_any()
                    .downcast_ref::<NumExpr>()
                    .map_or(false, |x| x.is_value()),
            _ => false,
        }
    }
}

impl CloneDynExpression for NumExpr {
    fn clone_dyn(&self) -> Box<dyn Expression> {
        Box::new(self.clone())
    }
}

impl Clone for NumExpr {
    fn clone(&self) -> Self {
        match self {
            Self::Succ(arg0) => Self::Succ(arg0.clone_dyn()),
            Self::Pred(arg0) => Self::Pred(arg0.clone_dyn()),
            Self::IsZero(arg0) => Self::IsZero(arg0.clone_dyn()),
            Self::Zero => Self::Zero,
        }
    }
}

#[cfg(test)]
mod test{

}