package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Rainha extends ChessPiece{

    public Rainha(Board board, Color color) {
        super(board, color);

    }

    @Override
    public String toString() {
        return "Q";
    }

     @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];

        Position p = new Position(0, 0);

        //acima da peça = mesma coluna - linha
        p.setValues(position.getRow() - 1, position.getColumn());
        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
            p.setRow(p.getRow() - 1);
        }
        if (getBoard().positionExists(p) && isThereOponentPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        //esquerda mesma linha - coluna
        p.setValues(position.getRow(), position.getColumn() - 1);
        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
            p.setColumn(p.getColumn() - 1);
        }
        if (getBoard().positionExists(p) && isThereOponentPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        //direita mesma linha coluna +
        p.setValues(position.getRow(), position.getColumn() + 1);
        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
            p.setColumn(p.getColumn() + 1);
        }
        if (getBoard().positionExists(p) && isThereOponentPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        //baixo da peça = mesma coluna + linha
        p.setValues(position.getRow() + 1, position.getColumn());
        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
            p.setRow(p.getRow() + 1);
        }
        if (getBoard().positionExists(p) && isThereOponentPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        //Noroeste -1 -1
        p.setValues(position.getRow() - 1, position.getColumn() - 1);
        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
            p.setValues(p.getRow() -1 , p.getColumn() -1);
        }
        if (getBoard().positionExists(p) && isThereOponentPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        //Nordeste -1 +1
        p.setValues(position.getRow() -1, position.getColumn() +1);
        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
            p.setValues(p.getRow() -1 , p.getColumn() +1);
        }
        if (getBoard().positionExists(p) && isThereOponentPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        //Sudeste +1 +1
        p.setValues(position.getRow() +1, position.getColumn() +1);
        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
            p.setValues(p.getRow() +1 , p.getColumn() +1);
        }
        if (getBoard().positionExists(p) && isThereOponentPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        //Sudoeste
        p.setValues(position.getRow() +1, position.getColumn() -1);
        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
            p.setValues(p.getRow() +1 , p.getColumn() -1);
        }
        if (getBoard().positionExists(p) && isThereOponentPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        return mat;
    }
    

}
