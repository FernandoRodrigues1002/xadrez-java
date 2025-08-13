
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import java.util.Scanner;

public class App {

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        ChessMatch chessMatch = new ChessMatch();

        //fica repetindo infinitamente
        while (true) {
            UI.printBoard(chessMatch.getPieces());
            System.out.println();

            System.out.println("Source: ");
            ChessPosition source = UI.readChessPosition(sc);

            System.out.println("Destino: ");
            ChessPosition target = UI.readChessPosition(sc);

            ChessPiece capturedPiece = chessMatch.peformChessMove(source, target);
        }

    }
}
