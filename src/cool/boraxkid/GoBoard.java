package cool.boraxkid;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;

//class definition for the Go board
class GoBoard extends Pane {
    // default constructor for the class
    public GoBoard() {
        this.horizontal = new Line[Go.GAME_BOARD_HEIGHT + 1];
        this.vertical = new Line[Go.GAME_BOARD_WIDTH + 1];
        this.horizontal_t = new Translate[Go.GAME_BOARD_HEIGHT + 1];
        this.vertical_t = new Translate[Go.GAME_BOARD_WIDTH + 1];
        this.board = new GoPiece[Go.GAME_BOARD_WIDTH][Go.GAME_BOARD_HEIGHT];
        this.surrounding = new int[3][3];
        this.can_reverse = new boolean[3][3];

        this.initialiseLinesBackground();
        this.initialiseRender();

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                this.surrounding[i][j] = 0;
                this.can_reverse[i][j] = false;
            }
        }

        this.resetGame();
    }

    // public method that will try to place a piece in the given x,y coordinate
    public void placePiece(final double x, final double y) {
        if (!this.in_play)
            return;

        int cellX = (int)(x / this.cell_width);
        int cellY = (int)(y / this.cell_height);

        if (!this.validCoords(cellX, cellY))
            return;
        if (this.board[cellX][cellY].getPiece() != Go.GAME_EMPTY_SPACE)
            return;

        if (!this.determineReverse(cellX, cellY))
            return;

        this.placeAndReverse(cellX, cellY);

        this.updateScores();

        this.swapPlayers();

        this.determineEndGame();

        if (this.in_play) {
            System.out.println("########################################");
            System.out.println("Current scores:");
            System.out.println("Player 1 (White): " + this.player1_score);
            System.out.println("Player 2 (Black): " + this.player2_score);
            System.out.println();
            if (!this.canMove())
                this.swapPlayers();
            if (this.current_player == Go.GAME_WHITE_PLAYER)
                System.out.println("Your turn player 1 (White)!");
            if (this.current_player == Go.GAME_BLACK_PLAYER)
                System.out.println("Your turn player 2 (Black)!");
        }
    }

    // overridden version of the resize method to give the board the correct size
    @Override
    public void resize(double width, double height) {
        super.resize(width, height);

        double newWidth = width - Go.APPLICATION_BORDER;
        double newHeight = height - Go.APPLICATION_BORDER;

        if (width > height)
            newWidth = newHeight;
        else
            newHeight = newWidth;

        this.cell_width = newWidth / Go.GAME_BOARD_WIDTH;
        this.cell_height = newHeight / Go.GAME_BOARD_HEIGHT;

        this.start_x = (width / 2) - (newWidth / 2);
        this.start_y = (height / 2) - (newHeight / 2);

        this.getChildren().remove(this.background);
        this.background = new Rectangle(width, height);
        this.background.setFill(Go.GAME_BACKGROUND_COLOR);
        this.getChildren().add(0, this.background);

        this.horizontalResizeRelocate(newWidth);
        this.verticalResizeRelocate(newHeight);

        this.pieceResizeRelocate();
    }

    // public method for resetting the game
    public void resetGame() {
        this.resetRenders();
        this.board[3][3].setPiece(Go.GAME_WHITE_PLAYER);
        this.board[4][4].setPiece(Go.GAME_WHITE_PLAYER);
        this.board[3][4].setPiece(Go.GAME_BLACK_PLAYER);
        this.board[4][3].setPiece(Go.GAME_BLACK_PLAYER);

        this.in_play = true;
        this.current_player = Go.GAME_BLACK_PLAYER;
        this.opposing = Go.GAME_WHITE_PLAYER;
        this.player1_score = 2;
        this.player2_score = 2;
    }

    // private method that will reset the renders
    private void resetRenders() {
        for (int i = 0; i < Go.GAME_BOARD_WIDTH; ++i) {
            for (int j = 0; j < Go.GAME_BOARD_HEIGHT; ++j) {
                this.board[i][j].setPiece(Go.GAME_EMPTY_SPACE);
            }
        }
    }

    // private method that will initialise the background and the lines
    private void initialiseLinesBackground() {
        this.background = new Rectangle(Go.APPLICATION_WIDTH, Go.APPLICATION_HEIGHT);
        this.background.setFill(Go.GAME_BACKGROUND_COLOR);
        this.getChildren().add(this.background);

        for (int i = 0; i < Go.GAME_BOARD_HEIGHT + 1; ++i) {
            this.horizontal[i] = new Line();

            this.horizontal[i].setStartX(0);
            this.horizontal[i].setStartY(0);
            this.horizontal[i].setEndY(0);

            this.horizontal_t[i] = new Translate(0, 0);
            this.horizontal[i].getTransforms().add(this.horizontal_t[i]);

            this.getChildren().add(this.horizontal[i]);
        }

        for (int i = 0; i < Go.GAME_BOARD_WIDTH + 1; ++i) {
            this.vertical[i] = new Line();

            this.vertical[i].setStartX(0);
            this.vertical[i].setEndX(0);
            this.vertical[i].setStartY(0);

            this.vertical_t[i] = new Translate(0, 0);
            this.vertical[i].getTransforms().add(this.vertical_t[i]);

            this.getChildren().add(this.vertical[i]);
        }
    }

    // private method for resizing and relocating the horizontal lines
    private void horizontalResizeRelocate(final double width) {
        for (int i = 0; i < Go.GAME_BOARD_HEIGHT + 1; ++i) {
            this.horizontal[i].setStartX(this.start_x);
            this.horizontal[i].setEndX(this.start_x + width);
            this.horizontal_t[i].setY(this.start_y + this.cell_height * i);
        }
    }

    // private method for resizing and relocating the vertical lines
    private void verticalResizeRelocate(final double height) {
        for (int i = 0; i < Go.GAME_BOARD_WIDTH + 1; ++i) {
            this.vertical[i].setStartY(this.start_y);
            this.vertical[i].setEndY(this.start_y + height);
            this.vertical_t[i].setX(this.start_x + this.cell_width * i);
        }
    }

    // private method for swapping the players
    private void swapPlayers() {
        if (this.current_player == Go.GAME_WHITE_PLAYER) {
            this.current_player = Go.GAME_BLACK_PLAYER;
            this.opposing = Go.GAME_WHITE_PLAYER;
        }
        else {
            this.current_player = Go.GAME_WHITE_PLAYER;
            this.opposing = Go.GAME_BLACK_PLAYER;
        }
    }

    // private method for updating the player scores
    private void updateScores() {
        int tmp;
        this.player1_score = 0;
        this.player2_score = 0;
        for (int i = 0; i < Go.GAME_BOARD_WIDTH; ++i) {
            for (int j = 0; j < Go.GAME_BOARD_HEIGHT; ++j) {
                tmp = this.board[i][j].getPiece();
                if (tmp == Go.GAME_WHITE_PLAYER)
                    ++this.player1_score;
                else if (tmp == Go.GAME_BLACK_PLAYER)
                    ++this.player2_score;
            }
        }
    }

    // private method for resizing and relocating all the pieces
    private void pieceResizeRelocate() {
        double cellX = this.cell_width * Go.GAME_PIECE_SCALE;
        double cellY = this.cell_height * Go.GAME_PIECE_SCALE;
        double offsetX = this.cell_width * ((1 - Go.GAME_PIECE_SCALE) / 2);
        double offsetY = this.cell_height * ((1 - Go.GAME_PIECE_SCALE) / 2);
        for (int i = 0; i < Go.GAME_BOARD_WIDTH; ++i) {
            for (int j = 0; j < Go.GAME_BOARD_HEIGHT; ++j) {
                this.board[i][j].resize(cellX, cellY);
                this.board[i][j].relocate(this.start_x + i * this.cell_width + offsetX, this.start_y + j * this.cell_height + offsetY);
            }
        }
    }

    // private method for determining which pieces surround x,y will update the
    // surrounding array to reflect this
    private void determineSurrounding(final int x, final int y) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                this.surrounding[i][j] = this.getPiece(x - 1 + i, y - 1 + j);
            }
        }
    }

    // private method for determining if a reverse can be made will update the can_reverse
    // array to reflect the answers will return true if a single reverse is found
    private boolean determineReverse(final int x, final int y) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                this.can_reverse[i][j] = false;
            }
        }
        this.determineSurrounding(x, y);
        if (!this.adjacentOpposingPiece())
            return (false);
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (i == 1 && j == 1) {
                    this.can_reverse[i][j] = false;
                }
                else if (this.surrounding[i][j] == this.opposing) {
                    this.can_reverse[i][j] = this.isReverseChain(x, y, i - 1, j - 1, this.current_player);
                }
            }
        }
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (this.can_reverse[i][j]) {
                    return (true);
                }
            }
        }
        return (false);
    }

    // private method for determining if a reverse can be made from a position (x,y) for
    // a player piece in the given direction (dx,dy) returns true if possible
    // assumes that the first piece has already been checked
    private boolean isReverseChain(final int x, final int y, final int dx, final int dy, final int player) {
        if (this.countChain(x, y, dx, dy) > 0) {
            return (true);
        }
        return (false);
    }

    // private method for determining if any of the surrounding pieces are an opposing
    // piece. if a single one exists then return true otherwise false
    private boolean adjacentOpposingPiece() {
        boolean surroundedByOpposite = false;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (this.surrounding[i][j] == this.opposing)
                    surroundedByOpposite = true;
            }
        }
        return (surroundedByOpposite);
    }

    // private method for placing a piece and reversing pieces
    private void placeAndReverse(final int x, final int y) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (this.can_reverse[i][j]) {
                    this.board[x][y].setPiece(this.current_player);
                    this.reverseChain(x, y, i - 1, j - 1);
                }
            }
        }
    }

    // private method to reverse a chain
    private void reverseChain(final int x, final int y, final int dx, final int dy) {
        int length = this.countChain(x, y, dx, dy);
        int tmpX = x + dx;
        int tmpY = y + dy;

        for (int i = 0; i < length; ++i) {
            this.board[tmpX][tmpY].setPiece(this.current_player);
            tmpX += dx;
            tmpY += dy;
        }
    }

    // private method for getting a piece on the board. this will return the board
    // value unless we access an index that doesnt exist. this is to make the code
    // for determing reverse chains much easier
    private int getPiece(final int x, final int y) {
        if (this.validCoords(x, y))
            return (this.board[x][y].getPiece());
        return (-1);
    }

    // private method that will determine if the end of the game has been reached
    private void determineEndGame() {
        int emptySpaces = 0;
        int p1Spaces = 0;
        int p2Spaces = 0;

        for (int i = 0; i < Go.GAME_BOARD_WIDTH; ++i) {
            for (int j = 0; j < Go.GAME_BOARD_HEIGHT; ++j) {
                if (this.board[i][j].getPiece() == Go.GAME_EMPTY_SPACE) {
                    ++emptySpaces;
                }
                else if (this.board[i][j].getPiece() == Go.GAME_WHITE_PLAYER) {
                    ++p1Spaces;
                }
                else if (this.board[i][j].getPiece() == Go.GAME_BLACK_PLAYER) {
                    ++p2Spaces;
                }
            }
        }

        boolean cpCanMove = this.canMove();
        this.swapPlayers();
        boolean opCanMove = this.canMove();
        this.swapPlayers();

        if (emptySpaces == 0 || p1Spaces == 0 || p2Spaces == 0 || (!cpCanMove && !opCanMove)) {
            this.in_play = false;
            this.determineWinner();
        }
    }

    // private method to determine if a player has a move available
    private boolean canMove() {
        for (int i = 0; i < Go.GAME_BOARD_WIDTH; ++i) {
            for (int j = 0; j < Go.GAME_BOARD_HEIGHT; ++j) {
                if (this.board[i][j].getPiece() == Go.GAME_EMPTY_SPACE) {
                    if (this.determineReverse(i, j))
                        return (true);
                }
            }
        }
        return (false);
    }

    // private method that determines who won the game
    private void determineWinner() {
        this.updateScores();
        System.out.println("The game is over! Here are the results:");
        if (this.player1_score == this.player2_score) {
            System.out.println("It's a DRAW! Both players have a score of " + this.player1_score + "!");
        }
        else if (this.player1_score > this.player2_score) {
            System.out.println("Player 1 (White) won with " + this.player1_score + " pieces, player 2 (Black) managed to place " + this.player2_score + " pieces.");
        }
        else {
            System.out.println("Player 2 (Black) won with " + this.player2_score + " pieces, player 1 (White) managed to place " + this.player1_score + " pieces.");
        }
    }

    // private method that will initialise everything in the render array
    private void initialiseRender() {
        for (int i = 0; i < Go.GAME_BOARD_WIDTH; ++i) {
            for (int j = 0; j < Go.GAME_BOARD_HEIGHT; ++j) {
                this.board[i][j] = new GoPiece(Go.GAME_EMPTY_SPACE);
            }
            this.getChildren().addAll(this.board[i]);
        }
    }

    private boolean validCoords(final int x, final int y) {
        if ((x >= 0 && x < Go.GAME_BOARD_WIDTH) && (y >= 0 && y < Go.GAME_BOARD_HEIGHT)) {
            return (true);
        }
        return (false);
    }

    private int countChain(final int x, final int y, final int dx, final int dy) {
        int tmpX = x + dx;
        int tmpY = y + dy;
        int length = 0;
        int oppositePlayer = this.board[tmpX][tmpY].getPiece();

        while (this.validCoords(tmpX, tmpY) && this.board[tmpX][tmpY].getPiece() == oppositePlayer) {
            tmpX += dx;
            tmpY += dy;
            ++length;
        }
        if (this.validCoords(tmpX, tmpY) && this.board[tmpX][tmpY].getPiece() == this.current_player) {
            return (length);
        }
        return (0);
    }

    // private fields that make the Go board work

    // rectangle that makes the background of the board
    private Rectangle background;
    // arrays for the lines that makeup the horizontal and vertical grid lines
    private Line[] horizontal;
    private Line[] vertical;
    // arrays holding translate objects for the horizontal and vertical grid lines
    private Translate[] horizontal_t;
    private Translate[] vertical_t;
    // arrays for the internal representation of the board and the pieces that are
    // in place
    private GoPiece[][] board;
    // the current player who is playing and who is his opposition
    private int current_player;
    private int opposing;
    // is the game currently in play
    private boolean in_play;
    // current scores of player 1 and player 2
    private int player1_score;
    private int player2_score;
    // the width and height of a cell in the board
    private double cell_width;
    private double cell_height;
    // 3x3 array that holds the pieces that surround a given piece
    private int[][] surrounding;
    // 3x3 array that determines if a reverse can be made in any direction
    private boolean[][] can_reverse;

    // Offset to center the board in the window
    private double start_x;
    private double start_y;
}
