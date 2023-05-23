use nom::{
    branch::Alt,
    bytes::complete::tag,
    character::complete::multispace0,
    combinator::map,
    error::ParseError,
    sequence::{delimited, Tuple},
    IResult,
};

use crate::{bx, expressions::binary_expressions::BinExpr};

pub struct BinExprParser;

fn ws<'a, F, O, E: ParseError<&'a str>>(inner: F) -> impl FnMut(&'a str) -> IResult<&'a str, O, E>
where
    F: FnMut(&'a str) -> IResult<&'a str, O, E>,
{
    delimited(multispace0, inner, multispace0)
}

impl BinExprParser {
    pub fn parse(s: &str) -> IResult<&str, Box<BinExpr>> {
        let true_tok = map(ws(tag("true")), |_| bx!(BinExpr::True));
        let false_tok = map(ws(tag("false")), |_| bx!(BinExpr::False));
        let if_parse = map(
            |s| {
                (
                    ws(tag("if")),
                    Self::parse,
                    ws(tag("then")),
                    Self::parse,
                    ws(tag("else")),
                    Self::parse,
                )
                    .parse(s)
            },
            |(_, cond, _, iftrue, _, iffalse)| {
                bx!(BinExpr::IfExpr {
                    cond,
                    iftrue,
                    iffalse,
                })
            },
        );

        (true_tok, false_tok, if_parse).choice(s)
    }
}

#[cfg(test)]
mod tests {
    use crate::{
        bx,
        parsers::binary_expression::{BinExpr, BinExprParser},
    };

    #[test]
    fn succ_parse_true() {
        assert_eq!(BinExprParser::parse("true"), Ok(("", bx!(BinExpr::True))))
    }
    #[test]
    fn succ_parse_false() {
        assert_eq!(BinExprParser::parse("false"), Ok(("", bx!(BinExpr::False))))
    }

    #[test]
    fn fail_parse_x() {
        assert!(BinExprParser::parse("x").is_err())
    }

    #[test]
    fn succ_parse_if_true_then_false_else_true() {
        assert_eq!(
            BinExprParser::parse("if true then false else true"),
            Ok((
                "",
                bx!(BinExpr::IfExpr {
                    cond: bx!(BinExpr::True),
                    iftrue: bx!(BinExpr::False),
                    iffalse: bx!(BinExpr::True),
                })
            ))
        );
    }

    #[test]
    fn succ_parse_nested() {
        assert_eq!(
            BinExprParser::parse("if if true then false else true then false else true"),
            Ok((
                "",
                bx!(BinExpr::IfExpr {
                    cond: bx!(BinExpr::IfExpr {
                        cond: bx!(BinExpr::True),
                        iftrue: bx!(BinExpr::False),
                        iffalse: bx!(BinExpr::True)
                    }),
                    iftrue: bx!(BinExpr::False),
                    iffalse: bx!(BinExpr::True)
                })
            ))
        )
    }
}
