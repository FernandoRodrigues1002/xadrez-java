package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.Bispo;
import chess.pieces.Cavalo;
import chess.pieces.King;
import chess.pieces.Peao;
import chess.pieces.Rainha;
import chess.pieces.Rook;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChessMatch {

    // toda partida tem um tabuleiro
    private Board board;
    private int turn;
    private Color currentPlayer;
    private List<Piece> piecesOnTheBoard = new ArrayList<>();
    private List<Piece> capturedPieces = new ArrayList<>();

    private Boolean check = false;
    private Boolean checkMate = false;

    private ChessPiece enPassantVulnerable;

    private ChessPiece promoted;


    public ChessMatch() {
        board = new Board(8, 8);
        turn = 1;
        currentPlayer = Color.WHITE;
        initialSetup();
    }

    public int getTurn() {
        return turn;
    }

    public boolean getCheck() {
        return check;
    }

    public boolean getCheckMate() {
        return checkMate;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public ChessPiece getenPassantVulnerable() {
        return enPassantVulnerable;
    }

    public ChessPiece getPromoted(){
        return  promoted;
    }

    public ChessPiece[][] getPieces() {
        ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                // linha i coluna J
                mat[i][j] = (ChessPiece) board.piece(i, j);
            }
        }
        return mat;
    }

    public boolean[][] possibleMoves(ChessPosition sourcePosition) {
        Position position = sourcePosition.toPosition();
        validateSourcePosition(position);

        return board.piece(position).possibleMoves();
    }

    public ChessPiece peformChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();

        validateSourcePosition(source);
        validateTargetPosition(source, target);

        Piece capturedPiece = makeMove(source, target);

        if (testCheck(currentPlayer)) {
            undoMove(source, target, capturedPiece);
            throw new ChessException("Você não pode se colocar em check");
        }

        ChessPiece movedPiece = (ChessPiece) board.piece(target);

        //Movimento especial Promoção
        promoted = null;
        if(movedPiece instanceof Peao){
            if((movedPiece.getColor() == Color.WHITE && target.getRow() == 0) || (movedPiece.getColor() == Color.BLACK && target.getRow() == 7)){
                promoted = (ChessPiece)board.piece(target);
                promoted = replacePromotedPiece("Q");
            }
        }

        check = (testCheck(opponent(currentPlayer))) ? true : false;

        if (testCheckMate(opponent(currentPlayer))) {
            checkMate = true;
        } else {
            nextTurn();
        }

        //Movimento especial En Passant
        if (movedPiece instanceof Peao && (target.getRow() == source.getRow() - 2 || target.getRow() == source.getRow() + 2)) {
            enPassantVulnerable = movedPiece;

        } else {
            enPassantVulnerable = null;
        }

        return (ChessPiece) capturedPiece;
    }

    private void validateSourcePosition(Position position) {
        if (!board.thereIsAPiece(position)) {
            throw new ChessException("Não existe uma peça na origem");
        }

        if (currentPlayer != ((ChessPiece) board.piece(position)).getColor()) {
            throw new ChessException("A peça escolida não é sua");
        }

        if (!board.piece(position).isThereAnyPossibleMoves()) {
            throw new ChessException("Não há movimentos possiveis para essa peça");
        }
    }

    private void validateTargetPosition(Position source, Position target) {
        if (!board.piece(source).possibleMoves(target)) {
            throw new ChessException("Não é possivel mudar a peça para essa posição");
        }
    }

    private Color opponent(Color color) {
        return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private ChessPiece king(Color color) {
        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color).collect(Collectors.toList());
        for (Piece p : list) {
            if (p instanceof King) {
                return (ChessPiece) p;
            }
        }
        throw new IllegalStateException("Não existe o rei da cor: " + color);
    }

    private boolean testCheck(Color color) {
        Position kingPosition = king(color).getChessPosition().toPosition();
        List<Piece> oponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == opponent(color)).collect(Collectors.toList());
        for (Piece p : oponentPieces) {
            boolean[][] mat = p.possibleMoves();
            if (mat[kingPosition.getRow()][kingPosition.getColumn()]) {
                return true;
            }
        }
        return false;
    }

    private boolean testCheckMate(Color color) {
        if (!testCheck(color)) {
            return false;
        }
        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color).collect(Collectors.toList());
        for (Piece p : list) {
            boolean[][] mat = p.possibleMoves();
            for (int i = 0; i < board.getRows(); i++) {
                for (int j = 0; j < board.getColumns(); j++) {
                    if (mat[i][j]) {
                        Position source = ((ChessPiece) p).getChessPosition().toPosition();
                        Position target = new Position(i, j);
                        Piece capturedPiece = makeMove(source, target);
                        boolean testCheck = testCheck(color);
                        undoMove(source, target, capturedPiece);
                        if (!testCheck) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private void placeNewPiece(char column, int row, ChessPiece piece) {
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
        piecesOnTheBoard.add(piece);

    }

    private void nextTurn() {
        turn++;
        currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    public ChessPiece replacePromotedPiece(String type){
        if (promoted == null){
            throw new IllegalStateException("Não há peça para ser promovida");
        }
        if(!type.equals("B") && !type.equals("C") && !type.equals("R") && !type.equals("Q")){
            return promoted;
        }

        Position pos = promoted.getChessPosition().toPosition();
        Piece p = board.removePiece(pos);
        piecesOnTheBoard.remove(p);

        ChessPiece newPiece = newPiece(type, promoted.getColor());
        board.placePiece(newPiece, pos);
        piecesOnTheBoard.add(newPiece);

        return newPiece;

    }

    private ChessPiece newPiece(String type, Color color){
        if(type.equals("B")) return new Bispo(board, color);
        if(type.equals("C")) return new Cavalo(board, color);
        if(type.equals("R")) return new Rook(board, color);
        return new Rainha(board, color);
    }

    private Piece makeMove(Position source, Position target) {
        ChessPiece p = (ChessPiece) board.removePiece(source);
        p.increaseMoveCount();
        Piece capturedPiece = board.removePiece(target);
        board.placePiece(p, target);
        if (capturedPiece != null) {
            piecesOnTheBoard.remove(capturedPiece);
            capturedPieces.add(capturedPiece);
        }

        //Roque pequeno
        if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
            Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
            Position targetT = new Position(source.getRow(), source.getColumn() + 1);

            ChessPiece rook = (ChessPiece) board.removePiece(sourceT);
            board.placePiece(rook, targetT);
            rook.increaseMoveCount();
        }

        //Roque grande
        if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
            Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
            Position targetT = new Position(source.getRow(), source.getColumn() - 1);

            ChessPiece rook = (ChessPiece) board.removePiece(sourceT);
            board.placePiece(rook, targetT);
            rook.increaseMoveCount();
        }

        //En Passant
        if (p instanceof Peao) {
            if (source.getColumn() != target.getColumn() && capturedPiece == null) {
				Position pawnPosition;
				if (p.getColor() == Color.WHITE) {
					pawnPosition = new Position(target.getRow() + 1, target.getColumn());
				}
				else {
					pawnPosition = new Position(target.getRow() - 1, target.getColumn());
				}
				capturedPiece = board.removePiece(pawnPosition);
				capturedPieces.add(capturedPiece);
				piecesOnTheBoard.remove(capturedPiece);
			}
        }

        return capturedPiece;
    }

    private void undoMove(Position source, Position target, Piece capturedPiece) {
        ChessPiece p = (ChessPiece) board.removePiece(target);
        p.decreaseMoveCount();
        board.placePiece(p, source);

        if (capturedPiece != null) {
            board.placePiece(capturedPiece, target);
            capturedPieces.remove(capturedPiece);

            piecesOnTheBoard.add(capturedPiece);
        }

        //Roque pequeno
        if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
            Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
            Position targetT = new Position(source.getRow(), source.getColumn() + 1);

            ChessPiece rook = (ChessPiece) board.removePiece(targetT);
            board.placePiece(rook, sourceT);
            rook.decreaseMoveCount();
        }

        //Roque grande
        if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
            Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
            Position targetT = new Position(source.getRow(), source.getColumn() - 1);

            ChessPiece rook = (ChessPiece) board.removePiece(targetT);
            board.placePiece(rook, sourceT);
            rook.decreaseMoveCount();
        }

        //En Passant
        if (p instanceof Peao) {
            if (source.getColumn() != target.getColumn() && capturedPiece == enPassantVulnerable) {
				ChessPiece pawn = (ChessPiece)board.removePiece(target);
				Position pawnPosition;
				if (p.getColor() == Color.WHITE) {
					pawnPosition = new Position(3, target.getColumn());
				}
				else {
					pawnPosition = new Position(4, target.getColumn());
				}
				board.placePiece(pawn, pawnPosition);
			}
        }
    }

    private void initialSetup() {
        placeNewPiece('a', 1, new Rook(board, Color.WHITE));
        placeNewPiece('b', 1, new Cavalo(board, Color.WHITE));
        placeNewPiece('c', 1, new Bispo(board, Color.WHITE));
        placeNewPiece('d', 1, new Rainha(board, Color.WHITE));
        placeNewPiece('e', 1, new King(board, Color.WHITE, this));
        placeNewPiece('f', 1, new Bispo(board, Color.WHITE));
        placeNewPiece('g', 1, new Cavalo(board, Color.WHITE));
        placeNewPiece('h', 1, new Rook(board, Color.WHITE));
        placeNewPiece('a', 2, new Peao(board, Color.WHITE, this));
        placeNewPiece('b', 2, new Peao(board, Color.WHITE, this));
        placeNewPiece('c', 2, new Peao(board, Color.WHITE, this));
        placeNewPiece('d', 2, new Peao(board, Color.WHITE, this));
        placeNewPiece('e', 2, new Peao(board, Color.WHITE, this));
        placeNewPiece('f', 2, new Peao(board, Color.WHITE, this));
        placeNewPiece('g', 2, new Peao(board, Color.WHITE, this));
        placeNewPiece('h', 2, new Peao(board, Color.WHITE, this));

        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
        placeNewPiece('b', 8, new Cavalo(board, Color.BLACK));
        placeNewPiece('c', 8, new Bispo(board, Color.BLACK));
        placeNewPiece('d', 8, new Rainha(board, Color.BLACK));
        placeNewPiece('e', 8, new King(board, Color.BLACK, this));
        placeNewPiece('f', 8, new Bispo(board, Color.BLACK));
        placeNewPiece('g', 8, new Cavalo(board, Color.BLACK));
        placeNewPiece('h', 8, new Rook(board, Color.BLACK));
        placeNewPiece('a', 7, new Peao(board, Color.BLACK, this));
        placeNewPiece('b', 7, new Peao(board, Color.BLACK, this));
        placeNewPiece('c', 7, new Peao(board, Color.BLACK, this));
        placeNewPiece('d', 7, new Peao(board, Color.BLACK, this));
        placeNewPiece('e', 7, new Peao(board, Color.BLACK, this));
        placeNewPiece('f', 7, new Peao(board, Color.BLACK, this));
        placeNewPiece('g', 7, new Peao(board, Color.BLACK, this));
        placeNewPiece('h', 7, new Peao(board, Color.BLACK, this));
    }
}
