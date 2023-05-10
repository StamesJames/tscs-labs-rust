pub enum NumExpr{
    Succ(NumExpr),
    Pred(NumExpr),
    IsZero(NumExpr),
    Zero,
    
}