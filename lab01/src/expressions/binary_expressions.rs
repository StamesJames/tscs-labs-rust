use std::fmt::Debug;
use expr_eq_derive::ExprEq;

use super::{Expression, AsAny, ExpEq};

#[derive(Debug, ExprEq)]
pub enum BinExpr {
    IfExpr {
        cond: Box<dyn Expression>,
        iftrue: Box<dyn Expression>,
        iffalse: Box<dyn Expression>,
    },
    True,
    False,
}

impl PartialEq for BinExpr {
    fn eq(&self, other: &Self) -> bool {
        match (self, other) {
            (
                Self::IfExpr {
                    cond: l_cond,
                    iftrue: l_iftrue,
                    iffalse: l_iffalse,
                },
                Self::IfExpr {
                    cond: r_cond,
                    iftrue: r_iftrue,
                    iffalse: r_iffalse,
                },
            ) => {
                l_cond.exp_eq(&**r_cond)
                    && l_iftrue.exp_eq(&**r_iftrue)
                    && l_iffalse.exp_eq(&**r_iffalse)
            }
            _ => core::mem::discriminant(self) == core::mem::discriminant(other),
        }
    }
}

impl Eq for BinExpr {}

impl Expression for BinExpr { 
    fn evaluate(self) -> Box<dyn Expression> {
        todo!()
    }
}