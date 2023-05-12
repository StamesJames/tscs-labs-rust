use any_eq_derive::PartialEqAnyEq;

use super::{AnyEq, AsAny, Expression};

use std::{fmt::Debug};

#[derive(Debug, PartialEqAnyEq)]
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
                l_cond.any_eq(r_cond.as_any())
                    && l_iftrue.any_eq(r_iftrue.as_any())
                    && l_iffalse.any_eq(r_iffalse.as_any())
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
