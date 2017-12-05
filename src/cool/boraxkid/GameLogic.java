package cool.boraxkid;

class GameLogic {

    public GameLogic(GoBoard goBoard) {
        this.goBoard = goBoard;

        this.surrounding = new int[3][3];
        this.can_reverse = new boolean[3][3];

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                this.surrounding[i][j] = 0;
                this.can_reverse[i][j] = false;
            }
        }

        this.resetGame();
    }

    // public method that will try to place a piece in the given x,y coordinate
    public void placePiece(final int x, final int y) {
        if (!this.in_play)
            return;

        if (this.getPiece(x, y) != 0)
            return;

        System.out.println("liberties: " + this.countLiberties(x, y));

        if (!this.isValidMove(x, y))
            return;
        this.goBoard.pieces[x][y].setPiece(this.current_player);
        this.swapPlayers();
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
                tmp = this.goBoard.pieces[i][j].getPiece();
                if (tmp == Go.GAME_WHITE_PLAYER)
                    ++this.player1_score;
                else if (tmp == Go.GAME_BLACK_PLAYER)
                    ++this.player2_score;
            }
        }
    }

    // private method for getting a piece on the board. this will return the board
    // value unless we access an index that doesnt exist. this is to make the code
    // for determing reverse chains much easier
    private int getPiece(final int x, final int y) {
        if (this.validCoords(x, y))
            return (this.goBoard.pieces[x][y].getPiece());
        return (-1);
    }

    private boolean validCoords(final int x, final int y) {
        if ((x >= 0 && x < Go.GAME_BOARD_WIDTH) && (y >= 0 && y < Go.GAME_BOARD_HEIGHT)) {
            return (true);
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

    private int countLiberties(final int x, final int y) {
        int liberties = 0;

        if (this.getPiece(x - 1, y) == 0)
            ++liberties;
        if (this.getPiece(x + 1, y) == 0)
            ++liberties;
        if (this.getPiece(x, y - 1) == 0)
            ++liberties;
        if (this.getPiece(x, y + 1) == 0)
            ++liberties;

        // int tmp;
        // if ((tmp = this.getPiece(x - 1, y)) == 0 || tmp == color)
        //     ++liberties;
        // if ((tmp = this.getPiece(x + 1, y)) == 0 || tmp == color)
        //     ++liberties;
        // if ((tmp = this.getPiece(x, y - 1)) == 0 || tmp == color)
        //     ++liberties;
        // if ((tmp = this.getPiece(x, y + 1)) == 0 || tmp == color)
        //     ++liberties;
        return (liberties);
    }

    // public method for resetting the game
    public void resetGame() {
        this.resetRenders();

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
                this.goBoard.pieces[i][j].setPiece(Go.GAME_EMPTY_SPACE);
            }
        }
    }

    public boolean isValidMove(int x, int y){
        if(this.goBoard.pieces[x][y].getPiece() != Go.GAME_EMPTY_SPACE)
            return (false);
        if (this.countLiberties(x, y) <= 0)
            return (false);
        return (true);
    }

    private GoBoard goBoard;
    // the current player who is playing and who is his opposition
    private int current_player;
    private int opposing;
    // is the game currently in play
    private boolean in_play;
    // current scores of player 1 and player 2
    private int player1_score;
    private int player2_score;
    // 3x3 array that holds the pieces that surround a given piece
    private int[][] surrounding;
    // 3x3 array that determines if a reverse can be made in any direction
    private boolean[][] can_reverse;
}
