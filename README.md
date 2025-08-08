## 1. App.java – Ponto de Entrada

Contém o método main que inicia a aplicação.

Cria uma partida de xadrez (ChessMatch) e chama o UI para imprimir o tabuleiro.

## 2. UI.java – Interface de Usuário no Console

Responsável apenas por exibir o tabuleiro no terminal.

printBoard(ChessPiece[][] pieces) imprime todas as posições, mostrando peças ou traços - onde não há peça.

Não contém regras de jogo, apenas exibição.

## 3. ChessMatch.java – Controlador da Partida

Representa uma partida de xadrez.

Contém um Board (tabuleiro genérico).

Inicializa o tabuleiro com tamanho 8x8.

Possui o método getPieces() que retorna a matriz de peças específicas de xadrez (ChessPiece).

## 4. ChessPiece.java – Peça de Xadrez

Herdada de Piece (genérica).

Adiciona o atributo Color (preto ou branco).

Toda ChessPiece já tem os atributos herdados de Piece:

position → coordenada no tabuleiro.

board → referência ao tabuleiro onde está.

## 5. Color.java – Enum de Cores

Define apenas:
BLACK
WHITE

## 6. Board.java – Tabuleiro Genérico

Representa um tabuleiro de qualquer jogo.

Contém número de linhas (rows) e colunas (columns).

Possui uma matriz Piece[][] pieces.

Métodos para recuperar peças por coordenadas (piece(row, column)) ou por Position.

## 7. Piece.java – Peça Genérica

Serve como molde para qualquer peça de jogo de tabuleiro.

Atributos:

Position position → localização no tabuleiro.

Board board → referência ao tabuleiro.

Não define cor, nome ou regras — isso é feito nas subclasses.

## 8 Position.java – Coordenada no Tabuleiro

Armazena:

row → linha.

column → coluna.

Usada para localizar peças e validar jogadas.

## Conceito de Herança no Projeto

As classes genéricas (Piece, Board, Position) funcionam como um molde que as classes específicas (como ChessPiece) herdam.

Por exemplo:

Piece define que toda peça terá uma position e estará dentro de um board.

ChessPiece herda isso e adiciona color (específico do xadrez).

Isso permite:

Reaproveitar código.

Criar novas peças ou jogos apenas estendendo as classes base.
