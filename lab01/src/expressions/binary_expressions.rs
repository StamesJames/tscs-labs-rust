extern crate any_eq_derive;
use crate::bx;

use super::{AnyEq, AsAny, AsExpression, Expression};
use any_eq_derive::{AnyEq, AsAny, PartialEqAnyEq};
use std::any::Any;
use std::fmt::Debug;

#[derive(Debug, AsAny, AnyEq, PartialEqAnyEq)]
pub enum BinExpr {
    IfExpr {
        cond: Box<dyn Expression>,
        iftrue: Box<dyn Expression>,
        iffalse: Box<dyn Expression>,
    },
    True,
    False,
}

impl Eq for BinExpr {}

impl Expression for BinExpr {
    fn evaluate(&self) -> Option<Box<dyn Expression>> {
        match self {
            BinExpr::IfExpr {
                cond,
                iftrue,
                iffalse,
            } => {
                if cond.evaluate() == Some(BinExpr::True.as_boxed_expr()) {
                    iftrue.evaluate()
                } else if cond.evaluate() == Some(BinExpr::False.as_boxed_expr()) {
                    iffalse.evaluate()
                } else {
                    None
                }
            }
            BinExpr::True => None,
            BinExpr::False => None,
        }
    }

    fn is_value(&self) -> bool {
        match self {
            Self::True | Self::False => true,
            _ => false
        }
    }
}

#[cfg(test)]
mod tests {
    use super::super::AnyEq;
    use super::super::AsAny;
    use super::*;
    use crate::bx;
    use crate::expressions::AsExpression;
    use BinExpr::{False, IfExpr, True};

    #[test]
    fn true_eval_true() {
        assert_eq!(None, True.evaluate())
    }

    #[test]
    fn fals_eval_false() {
        assert_eq!(None, False.evaluate())
    }

    #[test]
    fn if_true_then_false_else_true_eval_false() {
        assert_eq!(
            Some(False.as_boxed_expr()),
            IfExpr {
                cond: bx!(True),
                iftrue: bx!(False),
                iffalse: bx!(True)
            }
            .evaluate()
        )
    }

    #[test]
    fn if_if_true_then_false_else_true_then_false_else_true_eval_true() {
        assert_eq!(
            Some(True.as_boxed_expr()),
            IfExpr {
                cond: bx!(IfExpr {
                    cond: bx!(True),
                    iftrue: bx!(False),
                    iffalse: bx!(True)
                }),
                iftrue: bx!(False),
                iffalse: bx!(True)
            }
            .evaluate()
        )
    }

    #[test]
    fn bin_expr_any_eq() {
        assert!(True.any_eq(True.as_any()));
        assert!(False.any_eq(False.as_any()));
        assert!(IfExpr {
            cond: bx!(True),
            iftrue: bx!(False),
            iffalse: bx!(True)
        }
        .any_eq(
            IfExpr {
                cond: bx!(True),
                iftrue: bx!(False),
                iffalse: bx!(True)
            }
            .as_any()
        ));
        assert!(IfExpr {
            cond: bx!(IfExpr {
                cond: bx!(True),
                iftrue: bx!(False),
                iffalse: bx!(True)
            }),
            iftrue: bx!(False),
            iffalse: bx!(True)
        }
        .any_eq(
            IfExpr {
                cond: bx!(IfExpr {
                    cond: bx!(True),
                    iftrue: bx!(False),
                    iffalse: bx!(True)
                }),
                iftrue: bx!(False),
                iffalse: bx!(True)
            }
            .as_any()
        ));
    }
    #[test]
    fn bin_expr_any_neq() {
        assert!(!True.any_eq(False.as_any()));
        assert!(!False.any_eq(True.as_any()));
        assert!(!IfExpr {
            cond: bx!(False),
            iftrue: bx!(False),
            iffalse: bx!(True)
        }
        .any_eq(
            IfExpr {
                cond: bx!(True),
                iftrue: bx!(False),
                iffalse: bx!(True)
            }
            .as_any()
        ));
        assert!(!IfExpr {
            cond: bx!(True),
            iftrue: bx!(False),
            iffalse: bx!(True)
        }
        .any_eq(False.as_any()));
        assert!(!True.any_eq(
            IfExpr {
                cond: bx!(True),
                iftrue: bx!(False),
                iffalse: bx!(True)
            }
            .as_any()
        ));
        assert!(!IfExpr {
            cond: bx!(True),
            iftrue: bx!(False),
            iffalse: bx!(True)
        }
        .any_eq(
            IfExpr {
                cond: bx!(IfExpr {
                    cond: bx!(False),
                    iftrue: bx!(False),
                    iffalse: bx!(True)
                }),
                iftrue: bx!(False),
                iffalse: bx!(True)
            }
            .as_any()
        ));
    }

    #[test]
    fn bin_expr_eqs() {
        assert_eq!(True, True);
        assert_eq!(False, False);
        assert_eq!(
            IfExpr {
                cond: bx!(True),
                iftrue: bx!(False),
                iffalse: bx!(True)
            },
            IfExpr {
                cond: bx!(True),
                iftrue: bx!(False),
                iffalse: bx!(True)
            }
        );
        assert_eq!(
            IfExpr {
                cond: bx!(True),
                iftrue: bx!(False),
                iffalse: bx!(IfExpr {
                    cond: bx!(True),
                    iftrue: bx!(False),
                    iffalse: bx!(True)
                })
            },
            IfExpr {
                cond: bx!(True),
                iftrue: bx!(False),
                iffalse: bx!(IfExpr {
                    cond: bx!(True),
                    iftrue: bx!(False),
                    iffalse: bx!(True)
                })
            }
        );
        assert_eq!(
            IfExpr {
                cond: bx!(True),
                iftrue: bx!(IfExpr {
                    cond: bx!(True),
                    iftrue: bx!(False),
                    iffalse: bx!(True)
                }),
                iffalse: bx!(True)
            },
            IfExpr {
                cond: bx!(True),
                iftrue: bx!(IfExpr {
                    cond: bx!(True),
                    iftrue: bx!(False),
                    iffalse: bx!(True)
                }),
                iffalse: bx!(True)
            }
        );
        assert_eq!(
            IfExpr {
                cond: bx!(IfExpr {
                    cond: bx!(True),
                    iftrue: bx!(False),
                    iffalse: bx!(True)
                }),
                iftrue: bx!(False),
                iffalse: bx!(True)
            },
            IfExpr {
                cond: bx!(IfExpr {
                    cond: bx!(True),
                    iftrue: bx!(False),
                    iffalse: bx!(True)
                }),
                iftrue: bx!(False),
                iffalse: bx!(True)
            }
        );
    }
    #[test]
    fn bin_expr_neqs() {
        assert_ne!(True, False);
        assert_ne!(
            IfExpr {
                cond: bx!(True),
                iftrue: bx!(False),
                iffalse: bx!(True)
            },
            IfExpr {
                cond: bx!(False),
                iftrue: bx!(False),
                iffalse: bx!(True)
            }
        );
        assert_ne!(
            IfExpr {
                cond: bx!(True),
                iftrue: bx!(False),
                iffalse: bx!(IfExpr {
                    cond: bx!(True),
                    iftrue: bx!(False),
                    iffalse: bx!(True)
                })
            },
            IfExpr {
                cond: bx!(True),
                iftrue: bx!(False),
                iffalse: bx!(IfExpr {
                    cond: bx!(False),
                    iftrue: bx!(False),
                    iffalse: bx!(True)
                })
            }
        );
        assert_ne!(
            IfExpr {
                cond: bx!(True),
                iftrue: bx!(IfExpr {
                    cond: bx!(False),
                    iftrue: bx!(False),
                    iffalse: bx!(True)
                }),
                iffalse: bx!(True)
            },
            IfExpr {
                cond: bx!(True),
                iftrue: bx!(IfExpr {
                    cond: bx!(True),
                    iftrue: bx!(False),
                    iffalse: bx!(True)
                }),
                iffalse: bx!(True)
            }
        );
        assert_ne!(
            IfExpr {
                cond: bx!(IfExpr {
                    cond: bx!(True),
                    iftrue: bx!(False),
                    iffalse: bx!(True)
                }),
                iftrue: bx!(False),
                iffalse: bx!(True)
            },
            IfExpr {
                cond: bx!(IfExpr {
                    cond: bx!(False),
                    iftrue: bx!(False),
                    iffalse: bx!(True)
                }),
                iftrue: bx!(False),
                iffalse: bx!(True)
            }
        );
        assert_ne!(
            IfExpr {
                cond: bx!(True),
                iftrue: bx!(False),
                iffalse: bx!(True)
            },
            IfExpr {
                cond: bx!(IfExpr {
                    cond: bx!(False),
                    iftrue: bx!(False),
                    iffalse: bx!(True)
                }),
                iftrue: bx!(False),
                iffalse: bx!(True)
            }
        );
    }
}
