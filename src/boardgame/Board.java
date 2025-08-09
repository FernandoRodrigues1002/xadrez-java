package boardgame;

public class Board {
    private int rows;
    private int columns;

    //Matriz de peças
    //contém as peças ou null
    private Piece[][] pieces;

    public Board(int rows, int columns) {
        if(rows < 1 || columns < 1){
            throw new BoardException("ero ao criar o tabuleiro, é preciso de 1 ou mais clonua/linha");
        }
        this.rows = rows;
        this.columns = columns;
        pieces = new Piece[rows][columns];
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public Piece piece(int row, int column){
        if(!positionExists(row, column)){
            throw new BoardException("Posição fora do tabuleiro");
        }

        return pieces[row][column];
    }

    public Piece piece(Position position){
        if(!positionExists(position)){
            throw new BoardException("Posição fora do tabuleiro");
        }
        return pieces [position.getRow()] [position.getColumn()];
    }

    public void PlacePiece(Piece piece, Position position){
        if(thereIsAPiece(position)){
            throw new BoardException("Já existe uma peça na posição");
        }

        //pegando a cord e adicionando a peça passada
        pieces[position.getRow()][position.getColumn()] = piece;
        piece.position = position;
    }

    private Boolean positionExists(int row, int column){
        return row >=0 && row < rows && column >=0 && column < columns;
        
    }

    public Boolean positionExists(Position position){
        return positionExists(position.getRow(), position.getColumn());
    }

    public Boolean thereIsAPiece(Position position){
        if(positionExists(position)){
            throw new BoardException("A posição não existe");
        }
        return piece(position) != null;
    }
}
