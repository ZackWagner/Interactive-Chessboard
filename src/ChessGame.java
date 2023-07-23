import java.util.Random;
import java.util.ArrayList;
import java.io.File;
import processing.core.PApplet;
import processing.core.PImage;
import java.util.concurrent.TimeUnit;


public class ChessGame extends PApplet {

  private static PImage backgroundImage; // backgound image
  private static PImage menuImage;
  private static PImage pawnAtEndImage;

  private boolean botShouldMove = false;
  private boolean botNeedsToMove = false;
  private static ArrayList<Thing> things = new ArrayList<Thing>();
      // arraylist storing Thing objects
  private static Random randGen; // Generator of random numbers
  private static Piece selectedPiece;
  private static boolean whiteTurn;
  private static Piece whiteKing;
  private static Piece blackKing;
  private static boolean pawnAtEnd;
  private Piece pawnInQuestion;
  private static boolean whiteCanCastleLeft;
  private static boolean whiteCanCastleRight;
  private static boolean blackCanCastleLeft;
  private static boolean blackCanCastleRight;
  private Piece whiteRookLeft;
  private Piece whiteRookRight;
  private Piece blackRookLeft;
  private Piece blackRookRight;
  private Piece enpassantablePawn;
  private Thing botButton;
  private Thing humanButton;
  private boolean playingBot;
  private boolean onMenu;
  public static boolean gameOver;
  public static int winnerNum; // 0 = N/A, 1 = white 2 = black 3 = stalemate


  /**
   * Sets the size of the display window of this graphic application
   */
  @Override
  public void settings() {
    this.size(800, 600);
  }

  /**
   * Defines initial environment properties of this graphic application. This method initializes all
   * the data fields defined in this class.
   */
  @Override
  public void setup() {
    this.getSurface().setTitle("Chess Game"); // displays the title of the screen
    this.textAlign(3, 3); // sets the alignment of the text
    this.imageMode(3); // interprets the x and y position of an image to its center
    this.focused = true; // confirms that this screen is "focused", meaning
    // it is active and will accept mouse and keyboard input.
    Thing.setProcessing(this);
    randGen = new Random();
    onMenu = true;
    gameOver = false;
    botShouldMove = false;
    winnerNum = 0;
    backgroundImage = loadImage("images" + File.separator + "backgroundboard.png");
    menuImage = loadImage("images" + File.separator + "Menu.png");
    pawnAtEndImage = loadImage("images" + File.separator + "PawnAtEndMenu.png");
    whiteTurn = true;
    botButton = new Thing(340, 520, "BotButton.png");
    things.add(botButton);
    humanButton = new Thing(455, 520, "HumanButton.png");
    things.add(humanButton);
    Thing.setProcessing(this);

  }
  public void createBoard() {
    // create white pawns
    things.add(new WhitePawn(189, 450));
    things.add(new WhitePawn(249, 450));
    things.add(new WhitePawn(309, 450));
    things.add(new WhitePawn(369, 450));
    things.add(new WhitePawn(429, 450));
    things.add(new WhitePawn(489, 450));
    things.add(new WhitePawn(549, 450));
    things.add(new WhitePawn(609, 450));
    // create white rooks
    whiteRookLeft = new WhiteRook(189, 510);
    whiteRookRight = new WhiteRook(609, 510);
    things.add(whiteRookRight);
    things.add(whiteRookLeft);
    // create white bishops
    things.add(new WhiteBishop(489, 510));
    things.add(new WhiteBishop(309, 510));
    // create white knights
    things.add(new WhiteKnight(549, 510));
    things.add(new WhiteKnight(249, 510));
    // create white queen
    things.add(new WhiteQueen(369, 510));
    // create white king
    whiteKing = new WhiteKing(429, 510);
    things.add(whiteKing);
    // create black pawns
    things.add(new BlackPawn(189, 150));
    things.add(new BlackPawn(249, 150));
    things.add(new BlackPawn(309, 150));
    things.add(new BlackPawn(369, 150));
    things.add(new BlackPawn(429, 150));
    things.add(new BlackPawn(489, 150));
    things.add(new BlackPawn(549, 150));
    things.add(new BlackPawn(609, 150));
    // create black rooks
    blackRookLeft =  new BlackRook(189, 90);
    blackRookRight = (new BlackRook(609, 90));
    things.add(blackRookLeft);
    things.add(blackRookRight);
    // create black bishops
    things.add(new BlackBishop(489, 90));
    things.add(new BlackBishop(309, 90));
    // create black knights
    things.add(new BlackKnight(549, 90));
    things.add(new BlackKnight(249, 90));
    // create black queen
    things.add(new BlackQueen(369, 90));
    // create black king
    blackKing = new BlackKing(429, 90);
    things.add(blackKing);

    // square length/width = 60
    whiteCanCastleRight = true;
    whiteCanCastleLeft = true;
    blackCanCastleRight = true;
    blackCanCastleLeft = true;
    pawnAtEnd = false;
    enpassantablePawn = null;

  }

  /**
   * Callback method that draws and updates the application display window.
   */
  public void draw() {
    // set the background color and draw the background image to the center of the screen
    background(color(255, 218, 185));
    image(backgroundImage, width / 2, height / 2);
    if (onMenu) {
      image(menuImage, width / 2, height / 2);
    }
    if (pawnAtEnd) {
      image(pawnAtEndImage, 80, 300);
    }
    isGameOver();

    for (int i = 0; i < things.size(); i++) {
      things.get(i).draw();
    }
    for (int i = 0; i < things.size(); i++) {
      if (things.get(i) instanceof AvailableMoveDot || things.get(i) instanceof CastleMoveDot || things.get(i) instanceof EnPassantMoveDot) {
        things.get(i).draw();
      }
    }
    if (gameOver && !onMenu) {
      if (winnerNum ==2) {
        image(loadImage("images" + File.separator + "WhiteCheckmateMenu.png"), width / 2, height / 2);
      }
      if (winnerNum == 1) {
        image(loadImage("images" + File.separator + "BlackCheckmateMenu.png"), width / 2, height / 2);
      }
      if (winnerNum == 3) {
        image(loadImage("images" + File.separator + "StalemateMenu.png"), width / 2, height / 2);
      }
    }
    if (botNeedsToMove && !gameOver) {
      try {
        Thread.sleep(700);
      } catch (Exception e) {}
      botRandomMove();
      botNeedsToMove = false;
    }
    if (botShouldMove) {
      botNeedsToMove = true;
      botShouldMove = false;
    }
  }

  /**
   * Driver method to run this graphic application
   *
   * @param args list of input arguments if any
   */
  public static void main(String[] args) {
    PApplet.main("ChessGame");
  }

  /**
   * Callback method called each time the user presses the mouse
   */
  public void mousePressed() {
    if (gameOver) {
      things.clear();
      setup();
      botShouldMove = false;
      botNeedsToMove = false;
      onMenu = true;
    }
    if (onMenu) {
      for (int i = 0; i<things.size(); i++) {
        if (things.get(i).isMouseOver() && things.get(i).equals(botButton)) { // selected to play the bot
          playingBot = true;
          onMenu = false;
          things.remove(botButton);
          things.remove(humanButton);
          createBoard();
        }
        if (things.get(i).isMouseOver() && things.get(i).equals(humanButton)) {
          playingBot = false;
          onMenu = false;
          things.remove(botButton);
          things.remove(humanButton);
          createBoard();
        }
      }
    }
    else if (!playingBot) {
      // traverse the list of things and call mousePressed on all Clickable objects
      if (whiteTurn && !pawnAtEnd) {
        for (int i = 0; i < things.size(); i++) {
          if (things.get(i).isMouseOver() && things.get(i) instanceof Piece && ((Piece) things.get(i)).isWhite) {
            selectedPiece = (Piece) things.get(i);
            selectedPiece.mousePressed();
            for (int j = 0; j < things.size(); j++) {
              if (things.get(j) instanceof AvailableMoveDot) { //get rid of avialable move dots
                things.remove(j);
                j--;
              }
            }
            for (Location l : selectedPiece.possibleMoves) { // add new available move dots
              things.add(new AvailableMoveDot(l));
            }
            if (selectedPiece instanceof WhiteKing) { // king selected, check for castling
              if (whiteReadyForCastleLeft())
                things.add(new CastleMoveDot(309, 510)); // castling left
              if (whiteReadyForCastleRight())
                things.add(new CastleMoveDot(549, 510)); // castling right
            }
            if (selectedPiece instanceof WhitePawn && enpassantablePawn != null) { // pawn selected, check for enPassant
              if (selectedPiece.y == enpassantablePawn.y && (selectedPiece.x == enpassantablePawn.x - 60 || selectedPiece.x == enpassantablePawn.x + 60)) {
                things.add(new EnPassantMoveDot(enpassantablePawn.x, enpassantablePawn.y - 60));
              }
            }
            break;
          } else if (things.get(i).isMouseOver() && things.get(i) instanceof AvailableMoveDot) { // clicked on a move dot, moving piece
            if (things.get(i) instanceof EnPassantMoveDot) {
              selectedPiece.x = enpassantablePawn.x;
              selectedPiece.y = enpassantablePawn.y - 60;
              things.remove(enpassantablePawn);
              i--;
            }
            enpassantablePawn = null;
            if (selectedPiece instanceof WhitePawn) {
              if (things.get(i).y + 120 == selectedPiece.y) {
                enpassantablePawn = selectedPiece;
              }
            }

            if (things.get(i) instanceof CastleMoveDot) { //castling
              if (things.get(i).x < whiteKing.x) { // castling left
                whiteRookLeft.x = 369;

              } else { // castling right
                whiteRookRight.x = 489;

              }
            }
            // move piece to selected location
            selectedPiece.x = things.get(i).x;
            selectedPiece.y = things.get(i).y;

            if (selectedPiece instanceof WhiteKing) {
              whiteCanCastleLeft = false;
              whiteCanCastleRight = false;
            }
            if (selectedPiece instanceof WhiteRook) {
              if (selectedPiece.equals(whiteRookLeft))
                whiteCanCastleLeft = false;
              if (selectedPiece.equals(whiteRookRight))
                whiteCanCastleRight = false;
            }

            // get rid of dots and update avialable moves for peice
            for (int q = 0; q < things.size(); q++) {
              if (things.get(q) instanceof AvailableMoveDot) { //get rid of avialable move dots
                things.remove(q);
                q--;
              }
            }
            if (selectedPiece instanceof WhitePawn) { // if its a pawn, take away double move ability
              ((WhitePawn) selectedPiece).hasMoved = true;
            }
            selectedPiece.mousePressed(); // make new moves for the selected piece

            // if there is another piece at the location, remove it

            for (int k = 0; k < things.size(); k++) {
              if (things.get(k) instanceof Piece && !((Piece) things.get(k)).isWhite && things.get(k).x == selectedPiece.x && things.get(k).y == selectedPiece.y) {
                things.remove(k);
                break;
              }
            }
            if (selectedPiece instanceof WhitePawn && selectedPiece.y == 90) {
              pawnAtEnd = true;
              pawnInQuestion = selectedPiece;
            }
            selectedPiece = null;
            whiteTurn = !whiteTurn;
            break;
          }

        }
      }
      // Blacks Turn
      else if (!pawnAtEnd) {
        for (int i = 0; i < things.size(); i++) {
          if (things.get(i).isMouseOver() && things.get(i) instanceof Piece && !((Piece) things.get(
              i)).isWhite) {
            selectedPiece = (Piece) things.get(i);
            selectedPiece.mousePressed();
            for (int j = 0; j < things.size(); j++) {
              if (things.get(j) instanceof AvailableMoveDot) { //get rid of avialablemove dots
                things.remove(j);
                j--;
              }
            }
            for (Location l : selectedPiece.possibleMoves) { // add new available move dots
              things.add(new AvailableMoveDot(l));
            }

            if (selectedPiece instanceof BlackKing) { // king selected, check for castling
              if (blackReadyForCastleLeft())
                things.add(new CastleMoveDot(309, 90)); // castling left
              if (blackReadyForCastleRight())
                things.add(new CastleMoveDot(549, 90)); // castling right
            }
            if (selectedPiece instanceof BlackPawn && enpassantablePawn != null) { // pawn selected, check for enPassant
              if (selectedPiece.y == enpassantablePawn.y && (selectedPiece.x == enpassantablePawn.x - 60 || selectedPiece.x == enpassantablePawn.x + 60)) {
                things.add(new EnPassantMoveDot(enpassantablePawn.x, enpassantablePawn.y + 60));
              }
            }
            break;
          }
          if (things.get(i).isMouseOver() && things.get(i) instanceof AvailableMoveDot) { // clicked on a move dot, moving piece
            if (things.get(i) instanceof EnPassantMoveDot) { // en passant
              selectedPiece.x = enpassantablePawn.x;
              selectedPiece.y = enpassantablePawn.y - 60;
              things.remove(enpassantablePawn);
              i--;
            }
            enpassantablePawn = null;
            if (selectedPiece instanceof BlackPawn) {
              if (things.get(i).y - 120 == selectedPiece.y) {
                enpassantablePawn = selectedPiece;
              }
            }
            if (things.get(i) instanceof CastleMoveDot) { //castling
              if (things.get(i).x < blackKing.x) { // castling left
                blackRookLeft.x = 369;
              } else { // castling right
                blackRookRight.x = 489;
              }
            }

            // move piece to selected location
            selectedPiece.x = things.get(i).x;
            selectedPiece.y = things.get(i).y;

            if (selectedPiece instanceof BlackKing) {
              blackCanCastleLeft = false;
              blackCanCastleRight = false;
            }
            if (selectedPiece instanceof BlackRook) {
              if (selectedPiece.equals(blackRookLeft))
                blackCanCastleLeft = false;
              if (selectedPiece.equals(blackRookRight))
                blackCanCastleRight = false;
            }

            // get rid of dots and update avialable moves for peice
            for (int q = 0; q < things.size(); q++) {
              if (things.get(q) instanceof AvailableMoveDot) { //get rid of avialable move dots
                things.remove(q);
                q--;
              }
            }
            if (selectedPiece instanceof BlackPawn) { // if its a pawn, take away double move ability
              ((BlackPawn) selectedPiece).hasMoved = true;
            }
            selectedPiece.mousePressed(); // make new moves for the selected piece

            // if there is another piece at the location, remove it

            for (int k = 0; k < things.size(); k++) {
              if (things.get(k) instanceof Piece && ((Piece) things.get(k)).isWhite && things.get(k).x == selectedPiece.x && things.get(k).y == selectedPiece.y) {
                things.remove(k);
                break;
              }
            }
            if (selectedPiece instanceof BlackPawn && selectedPiece.y == 90) {
              pawnAtEnd = true;
              pawnInQuestion = selectedPiece;
            }
            selectedPiece = null;
            whiteTurn = !whiteTurn;
            break;
          }
        }
      }
    }
    else { // playing bot

      for (int i = 0; i < things.size(); i++) {
        if (things.get(i).isMouseOver() && things.get(i) instanceof Piece && ((Piece) things.get(i)).isWhite) {
          selectedPiece = (Piece) things.get(i);
          selectedPiece.mousePressed();
          for (int j = 0; j < things.size(); j++) {
            if (things.get(j) instanceof AvailableMoveDot) { //get rid of avialable move dots
              things.remove(j);
              j--;
            }
          }
          for (Location l : selectedPiece.possibleMoves) { // add new available move dots
            things.add(new AvailableMoveDot(l));
          }
          if (selectedPiece instanceof WhiteKing) { // king selected, check for castling
            if (whiteReadyForCastleLeft())
              things.add(new CastleMoveDot(309, 510)); // castling left
            if (whiteReadyForCastleRight())
              things.add(new CastleMoveDot(549, 510)); // castling right
          }
          if (selectedPiece instanceof WhitePawn && enpassantablePawn != null) { // pawn selected, check for enPassant
            if (selectedPiece.y == enpassantablePawn.y && (selectedPiece.x == enpassantablePawn.x - 60 || selectedPiece.x == enpassantablePawn.x + 60)) {
              things.add(new EnPassantMoveDot(enpassantablePawn.x, enpassantablePawn.y - 60));
            }
          }
          break;
        } else if (things.get(i).isMouseOver() && things.get(i) instanceof AvailableMoveDot) { // clicked on a move dot, moving piece
          if (things.get(i) instanceof EnPassantMoveDot) {
            selectedPiece.x = enpassantablePawn.x;
            selectedPiece.y = enpassantablePawn.y - 60;
            things.remove(enpassantablePawn);
            i--;
          }
          enpassantablePawn = null;
          if (selectedPiece instanceof WhitePawn) {
            if (things.get(i).y + 120 == selectedPiece.y) {
              enpassantablePawn = selectedPiece;
            }
          }

          if (things.get(i) instanceof CastleMoveDot) { //castling
            if (things.get(i).x < whiteKing.x) { // castling left
              whiteRookLeft.x = 369;

            } else { // castling right
              whiteRookRight.x = 489;

            }
          }
          // move piece to selected location
          selectedPiece.x = things.get(i).x;
          selectedPiece.y = things.get(i).y;

          if (selectedPiece instanceof WhiteKing) {
            whiteCanCastleLeft = false;
            whiteCanCastleRight = false;
          }
          if (selectedPiece instanceof WhiteRook) {
            if (selectedPiece.equals(whiteRookLeft))
              whiteCanCastleLeft = false;
            if (selectedPiece.equals(whiteRookRight))
              whiteCanCastleRight = false;
          }

          // get rid of dots and update avialable moves for peice
          for (int q = 0; q < things.size(); q++) {
            if (things.get(q) instanceof AvailableMoveDot) { //get rid of avialable move dots
              things.remove(q);
              q--;
            }
          }
          if (selectedPiece instanceof WhitePawn) { // if its a pawn, take away double move ability
            ((WhitePawn) selectedPiece).hasMoved = true;
          }
          selectedPiece.mousePressed(); // make new moves for the selected piece

          // if there is another piece at the location, remove it

          for (int k = 0; k < things.size(); k++) {
            if (things.get(k) instanceof Piece && !((Piece) things.get(k)).isWhite && things.get(k).x == selectedPiece.x && things.get(k).y == selectedPiece.y) {
              things.remove(k);
              break;
            }
          }
          if (selectedPiece instanceof WhitePawn && selectedPiece.y == 90) {
            pawnAtEnd = true;
            pawnInQuestion = selectedPiece;
          }
          selectedPiece = null;
          whiteTurn = !whiteTurn;
          botShouldMove = true;
          break;
        }

      }
    }
  }
  public void botRandomMove(){
    if (pawnAtEnd) {
      if (pawnInQuestion.isWhite) {
        return;
      }
      things.add(new BlackQueen(pawnInQuestion.x, pawnInQuestion.y));
      things.remove(pawnInQuestion);
      pawnAtEnd = false;
    }
    Random myRand = new Random();
    while (true) {
      int pieceNum = myRand.nextInt(things.size()-1);
      if (things.get(pieceNum) instanceof Piece && !((Piece) things.get(pieceNum)).isWhite) {
        ((Piece) things.get(pieceNum)).mousePressed();
        int numMoves = ((Piece) things.get(pieceNum)).possibleMoves.size();
        if (numMoves >= 1) {
          int moveNum = 0;
          if (numMoves > 1) {
            moveNum = myRand.nextInt(numMoves - 1);
          }
          Location selectedMove = ((Piece) things.get(pieceNum)).possibleMoves.get(moveNum);
          if (!movedToCheckBlack(((Piece) things.get(pieceNum)), selectedMove)) {
            things.get(pieceNum).x = selectedMove.x;
            things.get(pieceNum).y = selectedMove.y;
            selectedPiece = (Piece) (things.get(pieceNum));
            if (selectedPiece instanceof BlackPawn) { // if it's a pawn, take away double move ability
              ((BlackPawn) selectedPiece).hasMoved = true;
            }
            selectedPiece.mousePressed(); // make new moves for the selected piece

            // if there is another piece at the location, remove it

            for (int k = 0; k < things.size(); k++) {
              if (things.get(k) instanceof Piece && ((Piece) things.get(k)).isWhite && things.get(k).x == selectedPiece.x && things.get(k).y == selectedPiece.y) {
                things.remove(k);
                break;
              }
            }
            if (selectedPiece instanceof BlackPawn && selectedPiece.y == 90) {
              pawnAtEnd = true;
              pawnInQuestion = selectedPiece;
            }
            selectedPiece = null;
            break;
          }
        }
      }
    }
  }
  public static void calculatePossibleMovesWhiteKing(WhiteKing myKing){
      myKing.possibleMoves.clear();
      boolean isPossible1 = true; // use to test moving one space forward
      boolean isPossible2 = true; // use to test moving one space backward
      boolean isPossible3 = true; // use to test moving one space left
      boolean isPossible4 = true; // use to test moving one space right
      boolean isPossible5 = true; // use to test moving one space diagonal up and right
      boolean isPossible6 = true; // use to test moving one space diagonal up and left
      boolean isPossible7 = true; // use to test moving one space diagonal down and left
      boolean isPossible8 = true; // use to test moving one space diagonal down and right
      for (Thing curr : things) {
        if (curr instanceof Piece && ((Piece) curr).isWhite) {
          if (isHere(curr, myKing.x, myKing.y - 60) || myKing.y - 60 < 90) {
            isPossible1 = false;
          }
          if (isHere(curr, myKing.x, myKing.y + 60) || myKing.y + 60 > 510) {
            isPossible2 = false;
          }
          if (isHere(curr, myKing.x - 60, myKing.y) || myKing.x - 60 < 189) {
            isPossible3 = false;
          }
          if (isHere(curr, myKing.x + 60, myKing.y) || myKing.x + 60 > 609) {
            isPossible4 = false;
          }
          if (isHere(curr, myKing.x + 60, myKing.y - 60) || myKing.x + 60 > 609 || myKing.y - 60 < 90) {
            isPossible5 = false;
          }
          if (isHere(curr, myKing.x - 60, myKing.y - 60) || myKing.x - 60 < 189 || myKing.y - 60 < 90) {
            isPossible6 = false;
          }
          if (isHere(curr, myKing.x - 60, myKing.y + 60) || myKing.x - 60 < 189 || myKing.y + 60 > 510) {
            isPossible7 = false;
          }
          if (isHere(curr, myKing.x + 60, myKing.y + 60) || myKing.x + 60 > 609 || myKing.y + 60 > 510) {
            isPossible8 = false;
          }
        }
      }
      if (isPossible1 && !movedToCheckWhite(myKing, new Location(myKing.x, myKing.y - 60))) {
        myKing.possibleMoves.add(new Location(myKing.x, myKing.y - 60));
      }
      if (isPossible2 && !movedToCheckWhite(myKing, new Location(myKing.x, myKing.y + 60))) {
        myKing.possibleMoves.add(new Location(myKing.x, myKing.y + 60));
      }
      if (isPossible3 && !movedToCheckWhite(myKing, new Location(myKing.x - 60, myKing.y))) {
        myKing.possibleMoves.add(new Location(myKing.x - 60, myKing.y));
      }
      if (isPossible4 && !movedToCheckWhite(myKing, new Location(myKing.x + 60, myKing.y))) {
        myKing.possibleMoves.add(new Location(myKing.x + 60, myKing.y));
      }
      if (isPossible5 && !movedToCheckWhite(myKing, new Location(myKing.x + 60, myKing.y - 60))) {
        myKing.possibleMoves.add(new Location(myKing.x + 60, myKing.y - 60));
      }
      if (isPossible6 && !movedToCheckWhite(myKing, new Location(myKing.x - 60, myKing.y - 60))) {
        myKing.possibleMoves.add(new Location(myKing.x - 60, myKing.y - 60));
      }
      if (isPossible7 && !movedToCheckWhite(myKing, new Location(myKing.x - 60, myKing.y + 60))) {
        myKing.possibleMoves.add(new Location(myKing.x - 60, myKing.y + 60));
      }
      if (isPossible8 && !movedToCheckWhite(myKing, new Location(myKing.x + 60, myKing.y + 60))) {
        myKing.possibleMoves.add(new Location(myKing.x + 60, myKing.y + 60));
      }

  }

  public static void calculatePossibleMovesBlackKing(BlackKing myKing) {
    myKing.possibleMoves.clear();
    boolean isPossible1 = true; // use to test moving one space forward
    boolean isPossible2 = true; // use to test moving one space backward
    boolean isPossible3 = true; // use to test moving one space left
    boolean isPossible4 = true; // use to test moving one space right
    boolean isPossible5 = true; // use to test moving one space diagonal up and right
    boolean isPossible6 = true; // use to test moving one space diagonal up and left
    boolean isPossible7 = true; // use to test moving one space diagonal down and left
    boolean isPossible8 = true; // use to test moving one space diagonal down and right
    for (Thing curr : things) {
      if (curr instanceof Piece && !((Piece) curr).isWhite) {
        if (isHere(curr, myKing.x, myKing.y - 60) || myKing.y - 60 < 90) {
          isPossible1 = false;
        }
        if (isHere(curr, myKing.x, myKing.y + 60) || myKing.y + 60 > 510) {
          isPossible2 = false;
        }
        if (isHere(curr, myKing.x - 60, myKing.y) || myKing.x - 60 < 189) {
          isPossible3 = false;
        }
        if (isHere(curr, myKing.x + 60, myKing.y) || myKing.x + 60 > 609) {
          isPossible4 = false;
        }
        if (isHere(curr, myKing.x + 60, myKing.y - 60) || myKing.x + 60 > 609 || myKing.y - 60 < 90) {
          isPossible5 = false;
        }
        if (isHere(curr, myKing.x - 60, myKing.y - 60) || myKing.x - 60 < 189 || myKing.y - 60 < 90) {
          isPossible6 = false;
        }
        if (isHere(curr, myKing.x - 60, myKing.y + 60) || myKing.x - 60 < 189 || myKing.y + 60 > 510) {
          isPossible7 = false;
        }
        if (isHere(curr, myKing.x + 60, myKing.y + 60) || myKing.x + 60 > 609 || myKing.y + 60 > 510) {
          isPossible8 = false;
        }
      }
    }
    if (isPossible1 && !movedToCheckBlack(myKing, new Location(myKing.x, myKing.y - 60))) {
      myKing.possibleMoves.add(new Location(myKing.x, myKing.y - 60));
    }
    if (isPossible2 && !movedToCheckBlack(myKing, new Location(myKing.x, myKing.y + 60))) {
      myKing.possibleMoves.add(new Location(myKing.x, myKing.y + 60));
    }
    if (isPossible3 && !movedToCheckBlack(myKing, new Location(myKing.x - 60, myKing.y))) {
      myKing.possibleMoves.add(new Location(myKing.x - 60, myKing.y));
    }
    if (isPossible4 && !movedToCheckBlack(myKing, new Location(myKing.x + 60, myKing.y))) {
      myKing.possibleMoves.add(new Location(myKing.x + 60, myKing.y));
    }
    if (isPossible5 && !movedToCheckBlack(myKing, new Location(myKing.x + 60, myKing.y - 60))) {
      myKing.possibleMoves.add(new Location(myKing.x + 60, myKing.y - 60));
    }
    if (isPossible6 && !movedToCheckBlack(myKing, new Location(myKing.x - 60, myKing.y - 60))) {
      myKing.possibleMoves.add(new Location(myKing.x - 60, myKing.y - 60));
    }
    if (isPossible7 && !movedToCheckBlack(myKing, new Location(myKing.x - 60, myKing.y + 60))) {
      myKing.possibleMoves.add(new Location(myKing.x - 60, myKing.y + 60));
    }
    if (isPossible8 && !movedToCheckBlack(myKing, new Location(myKing.x + 60, myKing.y + 60))) {
      myKing.possibleMoves.add(new Location(myKing.x + 60, myKing.y + 60));
    }

  }
  public static void isGameOver() {
    if (things.isEmpty()) return;
    if (ChessGame.blackKing == null) return;
    if (ChessGame.whiteKing == null) return;
    boolean hasNoMovesBlack = true;
    boolean hasNoMovesWhite = true;
    for (int x = 0; x < things.size(); x++) {
      Thing i = things.get(x);
      if (i instanceof Piece) {
        if (((Piece) i).isWhite) {
          ((Piece) i).mousePressed();
          if (!((Piece) i).possibleMoves.isEmpty()) {
            hasNoMovesWhite = false;
          }
        }
        if (!((Piece) i).isWhite) {
          ((Piece) i).mousePressed();
          if (!((Piece) i).possibleMoves.isEmpty()) {
            hasNoMovesBlack = false;
          }
        }
      }
    }
    if (hasNoMovesBlack) {
      gameOver = true;
      if (isInCheckBlack()) {
        winnerNum = 2;
      }
      else {
        winnerNum = 3;
      }
    }
    if (hasNoMovesWhite) {
      gameOver = true;
      if (isInCheckWhite()) {
        winnerNum = 1;
      }
      else {
        winnerNum = 3;
      }
    }
  }
  public static void calculatePossibleMovesBlackQueen(BlackQueen myQueen) {

    myQueen.possibleMoves.clear();
    float moveX = myQueen.x + 60; // moving up & right
    float moveY = myQueen.y - 60;
    while (moveX <= 609 && moveY >= 90) {
      boolean toBreak = false;
      for (Thing curr : things) {
        if (isHere(curr, moveX, moveY)) {
          if (curr instanceof Piece) {
            if (!((Piece) curr).isWhite) {
              toBreak = true;
              break;
            }
            myQueen.possibleMoves.add(new Location(moveX, moveY));
            toBreak = true;
            break;
          }
        }
      }
      if (toBreak)
        break;
      myQueen.possibleMoves.add(new Location(moveX, moveY));
      moveX += 60;
      moveY -= 60;
    }
    moveX = myQueen.x - 60; // moving up & left
    moveY = myQueen.y - 60;
    while (moveX >= 189 && moveY >= 90) {
      boolean toBreak = false;
      for (Thing curr : things) {
        if (isHere(curr, moveX, moveY)) {
          if (curr instanceof Piece) {
            if (!((Piece) curr).isWhite) {
              toBreak = true;
              break;
            }
            myQueen.possibleMoves.add(new Location(moveX, moveY));
            toBreak = true;
            break;
          }
        }
      }
      if (toBreak)
        break;
      myQueen.possibleMoves.add(new Location(moveX, moveY));
      moveX -= 60;
      moveY -= 60;
    }
    moveX = myQueen.x - 60; // moving down & left
    moveY = myQueen.y + 60;
    while (moveX >= 189 && moveY <= 510) {
      boolean toBreak = false;
      for (Thing curr : things) {
        if (isHere(curr, moveX, moveY)) {
          if (curr instanceof Piece) {
            if (!((Piece) curr).isWhite) {
              toBreak = true;
              break;
            }
            myQueen.possibleMoves.add(new Location(moveX, moveY));
            toBreak = true;
            break;
          }
        }
      }
      if (toBreak)
        break;
      myQueen.possibleMoves.add(new Location(moveX, moveY));
      moveX -= 60;
      moveY += 60;
    }
    moveX = myQueen.x + 60; // moving down & right
    moveY = myQueen.y + 60;
    while (moveX <= 609 && moveY <= 510) {
      boolean toBreak = false;
      for (Thing curr : things) {
        if (isHere(curr, moveX, moveY)) {
          if (curr instanceof Piece) {
            if (!((Piece) curr).isWhite) {
              toBreak = true;
              break;
            }
            myQueen.possibleMoves.add(new Location(moveX, moveY));
            toBreak = true;
            break;
          }
        }
      }
      if (toBreak)
        break;
      myQueen.possibleMoves.add(new Location(moveX, moveY));
      moveX += 60;
      moveY += 60;
    }
    moveX = myQueen.x + 60; // moving right
    while (moveX <= 609) {
      boolean toBreak = false;
      for (Thing curr : things) {
        if (isHere(curr, moveX, myQueen.y)) {
          if (curr instanceof Piece) {
            if (!((Piece) curr).isWhite) {
              toBreak = true;
              break;
            }
            myQueen.possibleMoves.add(new Location(moveX, myQueen.y));
            toBreak = true;
            break;
          }
        }
      }
      if (toBreak)
        break;
      myQueen.possibleMoves.add(new Location(moveX, myQueen.y));
      moveX += 60;
    }
    moveX = myQueen.x - 60; // moving Left
    while (moveX >= 189) {
      boolean toBreak = false;
      for (Thing curr : things) {
        if (isHere(curr, moveX, myQueen.y)) {
          if (curr instanceof Piece) {
            if (!((Piece) curr).isWhite) {
              toBreak = true;
              break;
            }
            myQueen.possibleMoves.add(new Location(moveX, myQueen.y));
            toBreak = true;
            break;
          }
        }
      }
      if (toBreak)
        break;
      myQueen.possibleMoves.add(new Location(moveX, myQueen.y));
      moveX -= 60;

    }
    // moving up
    moveY = myQueen.y - 60;
    while (moveY >= 90) {
      boolean toBreak = false;
      for (Thing curr : things) {
        if (isHere(curr, myQueen.x, moveY)) {
          if (curr instanceof Piece) {
            if (!((Piece) curr).isWhite) {
              toBreak = true;
              break;
            }
            myQueen.possibleMoves.add(new Location(myQueen.x, moveY));
            toBreak = true;
            break;
          }
        }
      }
      if (toBreak)
        break;
      myQueen.possibleMoves.add(new Location(myQueen.x, moveY));
      moveY -= 60;
    }
    // moving down
    moveY = myQueen.y + 60;
    while (moveY <= 510) {
      boolean toBreak = false;
      for (Thing curr : things) {
        if (isHere(curr, myQueen.x, moveY)) {
          if (curr instanceof Piece) {
            if (!((Piece) curr).isWhite) {
              toBreak = true;
              break;
            }
            myQueen.possibleMoves.add(new Location(myQueen.x, moveY));
            toBreak = true;
            break;
          }
        }
      }
      if (toBreak)
        break;
      myQueen.possibleMoves.add(new Location(myQueen.x, moveY));
      moveY += 60;
    }
  }

  public static void calculatePossibleMovesWhiteQueen(WhiteQueen myQueen) {

    myQueen.possibleMoves.clear();
    float moveX = myQueen.x + 60; // moving up & right
    float moveY = myQueen.y - 60;
    while (moveX <= 609 && moveY >= 90) {
      boolean toBreak = false;
      for (Thing curr : things) {
        if (isHere(curr, moveX, moveY)) {
          if (curr instanceof Piece) {
            if (((Piece) curr).isWhite) {
              toBreak = true;
              break;
            }
            myQueen.possibleMoves.add(new Location(moveX, moveY));
            toBreak = true;
            break;
          }
        }
      }
      if (toBreak)
        break;
      myQueen.possibleMoves.add(new Location(moveX, moveY));
      moveX += 60;
      moveY -= 60;
    }
    moveX = myQueen.x - 60; // moving up & left
    moveY = myQueen.y - 60;
    while (moveX >= 189 && moveY >= 90) {
      boolean toBreak = false;
      for (Thing curr : things) {
        if (isHere(curr, moveX, moveY)) {
          if (curr instanceof Piece) {
            if (((Piece) curr).isWhite) {
              toBreak = true;
              break;
            }
            myQueen.possibleMoves.add(new Location(moveX, moveY));
            toBreak = true;
            break;
          }
        }
      }
      if (toBreak)
        break;
      myQueen.possibleMoves.add(new Location(moveX, moveY));
      moveX -= 60;
      moveY -= 60;
    }
    moveX = myQueen.x - 60; // moving down & left
    moveY = myQueen.y + 60;
    while (moveX >= 189 && moveY <= 510) {
      boolean toBreak = false;
      for (Thing curr : things) {
        if (isHere(curr, moveX, moveY)) {
          if (curr instanceof Piece) {
            if (((Piece) curr).isWhite) {
              toBreak = true;
              break;
            }
            myQueen.possibleMoves.add(new Location(moveX, moveY));
            toBreak = true;
            break;
          }
        }
      }
      if (toBreak)
        break;
      myQueen.possibleMoves.add(new Location(moveX, moveY));
      moveX -= 60;
      moveY += 60;
    }
    moveX = myQueen.x + 60; // moving down & right
    moveY = myQueen.y + 60;
    while (moveX <= 609 && moveY <= 510) {
      boolean toBreak = false;
      for (Thing curr : things) {
        if (isHere(curr, moveX, moveY)) {
          if (curr instanceof Piece) {
            if (((Piece) curr).isWhite) {
              toBreak = true;
              break;
            }
            myQueen.possibleMoves.add(new Location(moveX, moveY));
            toBreak = true;
            break;
          }
        }
      }
      if (toBreak)
        break;
      myQueen.possibleMoves.add(new Location(moveX, moveY));
      moveX += 60;
      moveY += 60;
    }
    moveX = myQueen.x + 60; // moving right
    while (moveX <= 609) {
      boolean toBreak = false;
      for (Thing curr : things) {
        if (isHere(curr, moveX, myQueen.y)) {
          if (curr instanceof Piece) {
            if (((Piece) curr).isWhite) {
              toBreak = true;
              break;
            }
            myQueen.possibleMoves.add(new Location(moveX, myQueen.y));
            toBreak = true;
            break;
          }
        }
      }
      if (toBreak)
        break;
      myQueen.possibleMoves.add(new Location(moveX, myQueen.y));
      moveX += 60;
    }
    moveX = myQueen.x - 60; // moving Left
    while (moveX >= 189) {
      boolean toBreak = false;
      for (Thing curr : things) {
        if (isHere(curr, moveX, myQueen.y)) {
          if (curr instanceof Piece) {
            if (((Piece) curr).isWhite) {
              toBreak = true;
              break;
            }
            myQueen.possibleMoves.add(new Location(moveX, myQueen.y));
            toBreak = true;
            break;
          }
        }
      }
      if (toBreak)
        break;
      myQueen.possibleMoves.add(new Location(moveX, myQueen.y));
      moveX -= 60;

    }
    // moving up
    moveY = myQueen.y - 60;
    while (moveY >= 90) {
      boolean toBreak = false;
      for (Thing curr : things) {
        if (isHere(curr, myQueen.x, moveY)) {
          if (curr instanceof Piece) {
            if (((Piece) curr).isWhite) {
              toBreak = true;
              break;
            }
            myQueen.possibleMoves.add(new Location(myQueen.x, moveY));
            toBreak = true;
            break;
          }
        }
      }
      if (toBreak)
        break;
      myQueen.possibleMoves.add(new Location(myQueen.x, moveY));
      moveY -= 60;
    }
    // moving down
    moveY = myQueen.y + 60;
    while (moveY <= 510) {
      boolean toBreak = false;
      for (Thing curr : things) {
        if (isHere(curr, myQueen.x, moveY)) {
          if (curr instanceof Piece) {
            if (((Piece) curr).isWhite) {
              toBreak = true;
              break;
            }
            myQueen.possibleMoves.add(new Location(myQueen.x, moveY));
            toBreak = true;
            break;
          }
        }
      }
      if (toBreak)
        break;
      myQueen.possibleMoves.add(new Location(myQueen.x, moveY));
      moveY += 60;
    }

  }

  public static void calculatePossibleMovesWhiteKnight(WhiteKnight myKnight) {
    myKnight.possibleMoves.clear();
    boolean isPossible1 = true; // up and to right
    boolean isPossible2 = true; // up and to the left
    boolean isPossible3 = true; // left and up
    boolean isPossible4 = true; // left and down
    boolean isPossible5 = true; // right and up
    boolean isPossible6 = true; // right and down
    boolean isPossible7 = true; // down and left
    boolean isPossible8 = true; // down and right
    for (Thing curr : things) {
      if (curr instanceof Piece && ((Piece) curr).isWhite) {
        if (isHere(curr, myKnight.x + 60,
            myKnight.y - 120) || myKnight.y - 120 < 90 || myKnight.x + 60 > 609) {
          isPossible1 = false;
        }
        if (isHere(curr, myKnight.x - 60,
            myKnight.y - 120) || myKnight.y - 120 < 90 || myKnight.x - 60 < 189) {
          isPossible2 = false;
        }
        if (isHere(curr, myKnight.x - 120,
            myKnight.y - 60) || myKnight.y - 60 < 90 || myKnight.x - 120 < 189) {
          isPossible3 = false;
        }
        if (isHere(curr, myKnight.x - 120,
            myKnight.y + 60) || myKnight.y + 60 > 510 || myKnight.x - 120 < 189) {
          isPossible4 = false;
        }
        if (isHere(curr, myKnight.x + 120,
            myKnight.y - 60) || myKnight.y - 60 < 90 || myKnight.x + 120 > 609) {
          isPossible5 = false;
        }
        if (isHere(curr, myKnight.x + 120,
            myKnight.y + 60) || myKnight.y + 60 > 510 || myKnight.x + 120 > 609) {
          isPossible6 = false;
        }
        if (isHere(curr, myKnight.x + 60,
            myKnight.y + 120) || myKnight.y + 120 > 510 || myKnight.x + 60 > 609) {
          isPossible7 = false;
        }
        if (isHere(curr, myKnight.x - 60,
            myKnight.y + 120) || myKnight.y + 120 > 510 || myKnight.x - 60 < 189) {
          isPossible8 = false;
        }
      }
    }
    if (isPossible1)
      myKnight.possibleMoves.add(new Location(myKnight.x + 60, myKnight.y - 120));
    if (isPossible2)
      myKnight.possibleMoves.add(new Location(myKnight.x - 60, myKnight.y - 120));
    if (isPossible3)
      myKnight.possibleMoves.add(new Location(myKnight.x - 120, myKnight.y - 60));
    if (isPossible4)
      myKnight.possibleMoves.add(new Location(myKnight.x - 120, myKnight.y + 60));
    if (isPossible5)
      myKnight.possibleMoves.add(new Location(myKnight.x + 120, myKnight.y - 60));
    if (isPossible6)
      myKnight.possibleMoves.add(new Location(myKnight.x + 120, myKnight.y + 60));
    if (isPossible7)
      myKnight.possibleMoves.add(new Location(myKnight.x + 60, myKnight.y + 120));
    if (isPossible8)
      myKnight.possibleMoves.add(new Location(myKnight.x - 60, myKnight.y + 120));
  }

  public static void calculatePossibleMovesBlackKnight(BlackKnight myKnight) {
    myKnight.possibleMoves.clear();
    boolean isPossible1 = true; // up and to right
    boolean isPossible2 = true; // up and to the left
    boolean isPossible3 = true; // left and up
    boolean isPossible4 = true; // left and down
    boolean isPossible5 = true; // right and up
    boolean isPossible6 = true; // right and down
    boolean isPossible7 = true; // down and left
    boolean isPossible8 = true; // down and right
    for (Thing curr : things) {
      if (curr instanceof Piece && !((Piece) curr).isWhite) {
        if (isHere(curr, myKnight.x + 60,
            myKnight.y - 120) || myKnight.y - 120 < 90 || myKnight.x + 60 > 609) {
          isPossible1 = false;
        }
        if (isHere(curr, myKnight.x - 60,
            myKnight.y - 120) || myKnight.y - 120 < 90 || myKnight.x - 60 < 189) {
          isPossible2 = false;
        }
        if (isHere(curr, myKnight.x - 120,
            myKnight.y - 60) || myKnight.y - 60 < 90 || myKnight.x - 120 < 189) {
          isPossible3 = false;
        }
        if (isHere(curr, myKnight.x - 120,
            myKnight.y + 60) || myKnight.y + 60 > 510 || myKnight.x - 120 < 189) {
          isPossible4 = false;
        }
        if (isHere(curr, myKnight.x + 120,
            myKnight.y - 60) || myKnight.y - 60 < 90 || myKnight.x + 120 > 609) {
          isPossible5 = false;
        }
        if (isHere(curr, myKnight.x + 120,
            myKnight.y + 60) || myKnight.y + 60 > 510 || myKnight.x + 120 > 609) {
          isPossible6 = false;
        }
        if (isHere(curr, myKnight.x + 60,
            myKnight.y + 120) || myKnight.y + 120 > 510 || myKnight.x + 60 > 609) {
          isPossible7 = false;
        }
        if (isHere(curr, myKnight.x - 60,
            myKnight.y + 120) || myKnight.y + 120 > 510 || myKnight.x - 60 < 189) {
          isPossible8 = false;
        }
      }
    }
    if (isPossible1)
      myKnight.possibleMoves.add(new Location(myKnight.x + 60, myKnight.y - 120));
    if (isPossible2)
      myKnight.possibleMoves.add(new Location(myKnight.x - 60, myKnight.y - 120));
    if (isPossible3)
      myKnight.possibleMoves.add(new Location(myKnight.x - 120, myKnight.y - 60));
    if (isPossible4)
      myKnight.possibleMoves.add(new Location(myKnight.x - 120, myKnight.y + 60));
    if (isPossible5)
      myKnight.possibleMoves.add(new Location(myKnight.x + 120, myKnight.y - 60));
    if (isPossible6)
      myKnight.possibleMoves.add(new Location(myKnight.x + 120, myKnight.y + 60));
    if (isPossible7)
      myKnight.possibleMoves.add(new Location(myKnight.x + 60, myKnight.y + 120));
    if (isPossible8)
      myKnight.possibleMoves.add(new Location(myKnight.x - 60, myKnight.y + 120));
  }

  public static void calculatePossibleMovesWhitePawn(WhitePawn myPawn) {
    myPawn.possibleMoves.clear();
    boolean isPossible = true; // use to test moving one space
    for (Thing curr : things) {
      if (curr instanceof Piece) {
        if (isHere(curr, myPawn.x, myPawn.y - 60)) {
          isPossible = false;
        } else if (isHere(curr, myPawn.x - 60, myPawn.y - 60) && !((Piece) curr).isWhite) { // diagonal attack left
          myPawn.possibleMoves.add(new Location(myPawn.x - 60, myPawn.y - 60));
        } else if (isHere(curr, myPawn.x + 60, myPawn.y - 60) && !((Piece) curr).isWhite) {
          myPawn.possibleMoves.add(new Location(myPawn.x + 60, myPawn.y - 60));
        }
      }
    }
    if (isPossible) {
      myPawn.possibleMoves.add(new Location(myPawn.x, myPawn.y - 60));
      if (!myPawn.hasMoved) { // use to test moving two spaces
        isPossible = true;
        for (Thing curr : things) {
          if (curr instanceof Piece) {
            if (isHere(curr, myPawn.x, myPawn.y - 120)) {
              isPossible = false;
            }
          }
        }
        if (isPossible) {
          myPawn.possibleMoves.add(new Location(myPawn.x, myPawn.y - 120));
        }
      }
    }
  }

  public static void calculatePossibleMovesBlackPawn(BlackPawn myPawn) {
    myPawn.possibleMoves.clear();
    boolean isPossible = true; // use to test moving one space
    for (Thing curr : things) {
      if (curr instanceof Piece) {
        if (isHere(curr, myPawn.x, myPawn.y + 60)) {
          isPossible = false;
        } else if (isHere(curr, myPawn.x - 60, myPawn.y + 60) && ((Piece) curr).isWhite) { // diagonal attack left
          myPawn.possibleMoves.add(new Location(myPawn.x - 60, myPawn.y + 60));
        } else if (isHere(curr, myPawn.x + 60, myPawn.y + 60) && ((Piece) curr).isWhite) {
          myPawn.possibleMoves.add(new Location(myPawn.x + 60, myPawn.y + 60));
        }
      }
    }
    if (isPossible) {
      myPawn.possibleMoves.add(new Location(myPawn.x, myPawn.y + 60));

      if (!myPawn.hasMoved) { // use to test moving two spaces
        isPossible = true;
        for (Thing curr : things) {
          if (curr instanceof Piece) {
            if (isHere(curr, myPawn.x, myPawn.y + 120)) {
              isPossible = false;
            }
          }
        }
        if (isPossible) {
          myPawn.possibleMoves.add(new Location(myPawn.x, myPawn.y + 120));
        }
      }
    }
  }

  public static void calculatePossibleMovesBlackBishop(BlackBishop myBishop) {
    myBishop.possibleMoves.clear();
    float moveX = myBishop.x + 60; // moving up & right
    float moveY = myBishop.y - 60;
    while (moveX <= 609 && moveY >= 90) {
      boolean toBreak = false;
      for (Thing curr : things) {
        if (isHere(curr, moveX, moveY)) {
          if (curr instanceof Piece) {
            if (!((Piece) curr).isWhite) {
              toBreak = true;
              break;
            }
            myBishop.possibleMoves.add(new Location(moveX, moveY));
            toBreak = true;
            break;
          }
        }
      }
      if (toBreak)
        break;
      myBishop.possibleMoves.add(new Location(moveX, moveY));
      moveX += 60;
      moveY -= 60;
    }
    moveX = myBishop.x - 60; // moving up & left
    moveY = myBishop.y - 60;
    while (moveX >= 189 && moveY >= 90) {
      boolean toBreak = false;
      for (Thing curr : things) {
        if (isHere(curr, moveX, moveY)) {
          if (curr instanceof Piece) {
            if (!((Piece) curr).isWhite) {
              toBreak = true;
              break;
            }
            myBishop.possibleMoves.add(new Location(moveX, moveY));
            toBreak = true;
            break;
          }
        }
      }
      if (toBreak)
        break;
      myBishop.possibleMoves.add(new Location(moveX, moveY));
      moveX -= 60;
      moveY -= 60;
    }
    moveX = myBishop.x - 60; // moving down & left
    moveY = myBishop.y + 60;
    while (moveX >= 189 && moveY <= 510) {
      boolean toBreak = false;
      for (Thing curr : things) {
        if (isHere(curr, moveX, moveY)) {
          if (curr instanceof Piece) {
            if (!((Piece) curr).isWhite) {
              toBreak = true;
              break;
            }
            myBishop.possibleMoves.add(new Location(moveX, moveY));
            toBreak = true;
            break;
          }
        }
      }
      if (toBreak)
        break;
      myBishop.possibleMoves.add(new Location(moveX, moveY));
      moveX -= 60;
      moveY += 60;
    }
    moveX = myBishop.x + 60; // moving down & right
    moveY = myBishop.y + 60;
    while (moveX <= 609 && moveY <= 510) {
      boolean toBreak = false;
      for (Thing curr : things) {
        if (isHere(curr, moveX, moveY)) {
          if (curr instanceof Piece) {
            if (!((Piece) curr).isWhite) {
              toBreak = true;
              break;
            }
            myBishop.possibleMoves.add(new Location(moveX, moveY));
            toBreak = true;
            break;
          }
        }
      }
      if (toBreak)
        break;
      myBishop.possibleMoves.add(new Location(moveX, moveY));
      moveX += 60;
      moveY += 60;
    }
  }

  public static void calculatePossibleMovesWhiteBishop(WhiteBishop myBishop) {
    myBishop.possibleMoves.clear();
    float moveX = myBishop.x + 60; // moving up & right
    float moveY = myBishop.y - 60;
    while (moveX <= 609 && moveY >= 90) {
      boolean toBreak = false;
      for (Thing curr : things) {
        if (isHere(curr, moveX, moveY)) {
          if (curr instanceof Piece) {
            if (((Piece) curr).isWhite) {
              toBreak = true;
              break;
            }
            myBishop.possibleMoves.add(new Location(moveX, moveY));
            toBreak = true;
            break;
          }
        }
      }
      if (toBreak)
        break;
      myBishop.possibleMoves.add(new Location(moveX, moveY));
      moveX += 60;
      moveY -= 60;
    }
    moveX = myBishop.x - 60; // moving up & left
    moveY = myBishop.y - 60;
    while (moveX >= 189 && moveY >= 90) {
      boolean toBreak = false;
      for (Thing curr : things) {
        if (isHere(curr, moveX, moveY)) {
          if (curr instanceof Piece) {
            if (((Piece) curr).isWhite) {
              toBreak = true;
              break;
            }
            myBishop.possibleMoves.add(new Location(moveX, moveY));
            toBreak = true;
            break;
          }
        }
      }
      if (toBreak)
        break;
      myBishop.possibleMoves.add(new Location(moveX, moveY));
      moveX -= 60;
      moveY -= 60;
    }
    moveX = myBishop.x - 60; // moving down & left
    moveY = myBishop.y + 60;
    while (moveX >= 189 && moveY <= 510) {
      boolean toBreak = false;
      for (Thing curr : things) {
        if (isHere(curr, moveX, moveY)) {
          if (curr instanceof Piece) {
            if (((Piece) curr).isWhite) {
              toBreak = true;
              break;
            }
            myBishop.possibleMoves.add(new Location(moveX, moveY));
            toBreak = true;
            break;
          }
        }
      }
      if (toBreak)
        break;
      myBishop.possibleMoves.add(new Location(moveX, moveY));
      moveX -= 60;
      moveY += 60;
    }
    moveX = myBishop.x + 60; // moving down & right
    moveY = myBishop.y + 60;
    while (moveX <= 609 && moveY <= 510) {
      boolean toBreak = false;
      for (Thing curr : things) {
        if (isHere(curr, moveX, moveY)) {
          if (curr instanceof Piece) {
            if (((Piece) curr).isWhite) {
              toBreak = true;
              break;
            }
            myBishop.possibleMoves.add(new Location(moveX, moveY));
            toBreak = true;
            break;
          }
        }
      }
      if (toBreak)
        break;
      myBishop.possibleMoves.add(new Location(moveX, moveY));
      moveX += 60;
      moveY += 60;
    }
  }

  public static void calculatePossibleMovesWhiteRook(WhiteRook myRook) {
    myRook.possibleMoves.clear();
    float moveX = myRook.x + 60; // moving right
    while (moveX <= 609) {
      boolean toBreak = false;
      for (Thing curr : things) {
        if (isHere(curr, moveX, myRook.y)) {
          if (curr instanceof Piece) {
            if (((Piece) curr).isWhite) {
              toBreak = true;
              break;
            }
            myRook.possibleMoves.add(new Location(moveX, myRook.y));
            toBreak = true;
            break;
          }
        }
      }
      if (toBreak)
        break;
      myRook.possibleMoves.add(new Location(moveX, myRook.y));
      moveX += 60;
    }
    moveX = myRook.x - 60; // moving Left
    while (moveX >= 189) {
      boolean toBreak = false;
      for (Thing curr : things) {
        if (isHere(curr, moveX, myRook.y)) {
          if (curr instanceof Piece) {
            if (((Piece) curr).isWhite) {
              toBreak = true;
              break;
            }
            myRook.possibleMoves.add(new Location(moveX, myRook.y));
            toBreak = true;
            break;
          }
        }
      }
      if (toBreak)
        break;
      myRook.possibleMoves.add(new Location(moveX, myRook.y));
      moveX -= 60;

    }
    // moving up
    float moveY = myRook.y - 60;
    while (moveY >= 90) {
      boolean toBreak = false;
      for (Thing curr : things) {
        if (isHere(curr, myRook.x, moveY)) {
          if (curr instanceof Piece) {
            if (((Piece) curr).isWhite) {
              toBreak = true;
              break;
            }
            myRook.possibleMoves.add(new Location(myRook.x, moveY));
            toBreak = true;
            break;
          }
        }
      }
      if (toBreak)
        break;
      myRook.possibleMoves.add(new Location(myRook.x, moveY));
      moveY -= 60;
    }
    // moving down
    moveY = myRook.y + 60;
    while (moveY <= 510) {
      boolean toBreak = false;
      for (Thing curr : things) {
        if (isHere(curr, myRook.x, moveY)) {
          if (curr instanceof Piece) {
            if (((Piece) curr).isWhite) {
              toBreak = true;
              break;
            }
            myRook.possibleMoves.add(new Location(myRook.x, moveY));
            toBreak = true;
            break;
          }
        }
      }
      if (toBreak)
        break;
      myRook.possibleMoves.add(new Location(myRook.x, moveY));
      moveY += 60;
    }
  }

  public static void calculatePossibleMovesBlackRook(BlackRook myRook) {
    myRook.possibleMoves.clear();
    float moveX = myRook.x + 60; // moving right
    while (moveX <= 609) {
      boolean toBreak = false;
      for (Thing curr : things) {
        if (isHere(curr, moveX, myRook.y)) {
          if (curr instanceof Piece) {
            if (!((Piece) curr).isWhite) {
              toBreak = true;
              break;
            }
            myRook.possibleMoves.add(new Location(moveX, myRook.y));
            toBreak = true;
            break;
          }
        }
      }
      if (toBreak)
        break;
      myRook.possibleMoves.add(new Location(moveX, myRook.y));
      moveX += 60;
    }
    moveX = myRook.x - 60; // moving Left
    while (moveX >= 189) {
      boolean toBreak = false;
      for (Thing curr : things) {
        if (isHere(curr, moveX, myRook.y)) {
          if (curr instanceof Piece) {
            if (!((Piece) curr).isWhite) {
              toBreak = true;
              break;
            }
            myRook.possibleMoves.add(new Location(moveX, myRook.y));
            toBreak = true;
            break;
          }
        }
      }
      if (toBreak)
        break;
      myRook.possibleMoves.add(new Location(moveX, myRook.y));
      moveX -= 60;

    }
    // moving up
    float moveY = myRook.y - 60;
    while (moveY >= 90) {
      boolean toBreak = false;
      for (Thing curr : things) {
        if (isHere(curr, myRook.x, moveY)) {
          if (curr instanceof Piece) {
            if (!((Piece) curr).isWhite) {
              toBreak = true;
              break;
            }
            myRook.possibleMoves.add(new Location(myRook.x, moveY));
            toBreak = true;
            break;
          }
        }
      }
      if (toBreak)
        break;
      myRook.possibleMoves.add(new Location(myRook.x, moveY));
      moveY -= 60;
    }
    // moving down
    moveY = myRook.y + 60;
    while (moveY <= 510) {
      boolean toBreak = false;
      for (Thing curr : things) {
        if (isHere(curr, myRook.x, moveY)) {
          if (curr instanceof Piece) {
            if (!((Piece) curr).isWhite) {
              toBreak = true;
              break;
            }
            myRook.possibleMoves.add(new Location(myRook.x, moveY));
            toBreak = true;
            break;
          }
        }
      }
      if (toBreak)
        break;
      myRook.possibleMoves.add(new Location(myRook.x, moveY));
      moveY += 60;
    }
  }

  /**
   * Helper method to determine if a test object is at a given x,y coordinate
   *
   * @param test object to test
   * @param x    x-coordinate
   * @param y    y-coordinate
   * @return true if it is here, false otherwise
   */
  private static boolean isHere(Thing test, float x, float y) {
    if (test.x == x && test.y == y) {
      return true;
    }
    return false;
  }

  private static boolean isInCheckWhite() {
    for (Thing curr : things) {
      if (curr instanceof Piece && !((Piece) curr).isWhite && !curr.equals(blackKing)) {
        ((Piece) curr).mousePressed(null);
        for (Location move : ((Piece) curr).possibleMoves) {
          if (isHere(whiteKing, move.x, move.y)) {
            return true;
          }
        }
      }
    }
    if (whiteKing.x <= blackKing.x + 60 && whiteKing.x >= blackKing.x - 60 && whiteKing.y >= blackKing.y - 60 && whiteKing.y <= blackKing.y + 60) {
      return true;
    }
    return false;
  }

  private static boolean isInCheckBlack() {
    for (Thing curr : things) {
      if (curr instanceof Piece && ((Piece) curr).isWhite && !curr.equals(whiteKing)) {
        ((Piece) curr).mousePressed(null);
        for (Location move : ((Piece) curr).possibleMoves) {
          if (isHere(blackKing, move.x, move.y)) {
            return true;
          }
        }
      }
    }
    if (blackKing.x >= whiteKing.x - 60 && blackKing.x <= whiteKing.x + 60 && blackKing.y >= whiteKing.y - 60 && blackKing.y <= whiteKing.x + 60) {
      return true;
    }
    return false;
  }

  public static boolean movedToCheckWhite(Piece movedPiece, Location move) {
    Location originalLocation = new Location(movedPiece.x, movedPiece.y);
    Thing temp = null;
    boolean toReturn = false;
    for (int i = 0; i < things.size(); i++) { // temporarily remove piece at the move location
      if (things.get(i).x == move.x && things.get(i).y == move.y && things.get(i) instanceof Piece) {
        temp = things.get(i); // temporarily store piece
        things.remove(i);
        break;
      }
    }
    movedPiece.x = move.x;
    movedPiece.y = move.y;
    toReturn = isInCheckWhite();
    movedPiece.x = originalLocation.x;
    movedPiece.y = originalLocation.y;
    if (temp != null) {
      things.add(temp);
    }
    return toReturn;
  }

  public static boolean movedToCheckBlack(Piece movedPiece, Location move) {
    Location originalLocation = new Location(movedPiece.x, movedPiece.y);
    Thing temp = null;
    boolean toReturn = false;
    for (int i = 0; i < things.size(); i++) { // temporarily remove piece at the move location
      if (things.get(i).x == move.x && things.get(i).y == move.y && things.get(i) instanceof Piece) {
        temp = things.get(i); // temporarily store piece
        things.remove(i);
        break;
      }
    }
    movedPiece.x = move.x;
    movedPiece.y = move.y;
    toReturn = isInCheckBlack();
    movedPiece.x = originalLocation.x;
    movedPiece.y = originalLocation.y;
    if (temp != null) {
      things.add(temp);
    }
    return toReturn;
  }

  private static boolean whiteReadyForCastleLeft() {
    if (!whiteCanCastleLeft) return false;
    if (isInCheckWhite()) return false;
    for (Thing curr: things) {
      if (curr instanceof Piece) {
        if (isHere(curr,249, 510) || isHere(curr, 309, 510) || isHere(curr, 369, 510)) return false;
      }
    }
    if (movedToCheckWhite(whiteKing, new Location(309, 510))) return false; // cant castle into check
    if (movedToCheckWhite(whiteKing, new Location(369, 510))) return false; // cant castle through check
    return true;
  }
  private static boolean whiteReadyForCastleRight() {
    if (!whiteCanCastleRight) return false;
    if (isInCheckWhite()) return false; // cant castle out of check
    for (Thing curr: things) {
      if (curr instanceof Piece) { // cant castle into nonempty space
        if (isHere(curr,549, 510) || isHere(curr, 489, 510)) return false;
      }
    }
    if (movedToCheckWhite(whiteKing, new Location(549, 510))) return false; // cant castle into check
    if (movedToCheckWhite(whiteKing, new Location(489, 510))) return false; // cant castle through check
    return true;
  }
  private static boolean blackReadyForCastleLeft() {
    if (!blackCanCastleLeft) return false;
    if (isInCheckBlack()) return false;
    for (Thing curr: things) {
      if (curr instanceof Piece) {
        if (isHere(curr,249, 90) || isHere(curr, 309, 90) || isHere(curr, 369, 90)) return false;
      }
    }
    if (movedToCheckBlack(blackKing, new Location(309, 90))) return false; // cant castle into check
    if (movedToCheckBlack(blackKing, new Location(369, 90))) return false; // cant castle through check

    return true;
  }
  private static boolean blackReadyForCastleRight() {
    if (!blackCanCastleRight) return false;
    if (isInCheckBlack()) return false; // cant castle out of check
    for (Thing curr: things) {
      if (curr instanceof Piece) { // cant castle into nonempty space
        if (isHere(curr,549, 90) || isHere(curr, 489, 90)) return false;
      }
    }
    if (movedToCheckBlack(blackKing, new Location(549, 90))) return false; // cant castle into check
    if (movedToCheckBlack(blackKing, new Location(489, 90))) return false; // cant castle through check

    return true;
  }

  public boolean isMated(boolean whiteTurn) {
    for (Thing curr: things) {
      if (curr instanceof Piece && ((Piece) curr).isWhite == whiteTurn) {
       ((Piece) curr).mousePressed();
       if (!((Piece) curr).possibleMoves.isEmpty()) return false;
      }
    }
    return true;
  }

  /**
   * Callback method called each time the user presses a key
   */

  public void keyPressed() {
    int startLocIndex = 0;
    switch (Character.toUpperCase(key)) {
      case 'Q':
        if (pawnAtEnd) {
          if (pawnInQuestion.isWhite) {
            things.remove(pawnInQuestion);
            things.add(new WhiteQueen(pawnInQuestion.x, pawnInQuestion.y));
          }
          else {
            things.remove(pawnInQuestion);
            things.add(new BlackQueen(pawnInQuestion.x, pawnInQuestion.y));
          }
          pawnAtEnd = false;

        }
      case 'K':
        if (pawnAtEnd) {
          if (pawnInQuestion.isWhite) {
            things.remove(pawnInQuestion);
            things.add(new WhiteKnight(pawnInQuestion.x, pawnInQuestion.y));
          }
          else {
            things.remove(pawnInQuestion);
            things.add(new BlackKnight(pawnInQuestion.x, pawnInQuestion.y));
          }
          pawnAtEnd = false;
        }
      case 'R':
        if (pawnAtEnd) {
          if (pawnInQuestion.isWhite) {
            things.remove(pawnInQuestion);
            things.add(new WhiteRook(pawnInQuestion.x, pawnInQuestion.y));
          }
          else {
            things.remove(pawnInQuestion);
            things.add(new BlackRook(pawnInQuestion.x, pawnInQuestion.y));
          }
          pawnAtEnd = false;
        }
      case 'B':
        if (pawnAtEnd) {
          if (pawnInQuestion.isWhite) {
            things.remove(pawnInQuestion);
            things.add(new WhiteBishop(pawnInQuestion.x, pawnInQuestion.y));
          }
          else {
            things.remove(pawnInQuestion);
            things.add(new BlackBishop(pawnInQuestion.x, pawnInQuestion.y));
          }
          pawnAtEnd = false;
        }
        /*
      case 'C':
        things.clear();
        whiteTurn = true;
        // create white pawns
        things.add(new WhitePawn(189, 450));
        things.add(new WhitePawn(249, 450));
        things.add(new WhitePawn(309, 450));
        things.add(new WhitePawn(369, 450));
        things.add(new WhitePawn(429, 450));
        things.add(new WhitePawn(489, 450));
        things.add(new WhitePawn(549, 450));
        things.add(new WhitePawn(609, 450));
        // create white rooks
        whiteRookLeft = new WhiteRook(189, 510);
        whiteRookRight = new WhiteRook(609, 510);
        things.add(whiteRookRight);
        things.add(whiteRookLeft);
        // create white bishops
        things.add(new WhiteBishop(489, 510));
        things.add(new WhiteBishop(309, 510));
        // create white knights
        things.add(new WhiteKnight(549, 510));
        things.add(new WhiteKnight(249, 510));
        // create white queen
        things.add(new WhiteQueen(369, 510));
        // create white king
        whiteKing = new WhiteKing(429, 510);
        things.add(whiteKing);
        // create black pawns
        things.add(new BlackPawn(189, 150));
        things.add(new BlackPawn(249, 150));
        things.add(new BlackPawn(309, 150));
        things.add(new BlackPawn(369, 150));
        things.add(new BlackPawn(429, 150));
        things.add(new BlackPawn(489, 150));
        things.add(new BlackPawn(549, 150));
        things.add(new BlackPawn(609, 150));
        // create black rooks
        blackRookLeft =  new BlackRook(189, 90);
        blackRookRight = (new BlackRook(609, 90));
        things.add(blackRookLeft);
        things.add(blackRookRight);
        // create black bishops
        things.add(new BlackBishop(489, 90));
        things.add(new BlackBishop(309, 90));
        // create black knights
        things.add(new BlackKnight(549, 90));
        things.add(new BlackKnight(249, 90));
        // create black queen
        things.add(new BlackQueen(369, 90));
        // create black king
        blackKing = new BlackKing(429, 90);
        things.add(blackKing);

        // square length/width = 60
        whiteCanCastleRight = true;
        whiteCanCastleLeft = true;
        blackCanCastleRight = true;
        blackCanCastleLeft = true;
        pawnAtEnd = false;
        enpassantablePawn = null;
*/
    }
    if (pawnInQuestion != null && playingBot && pawnInQuestion.isWhite) {
      pawnAtEnd = false;
      botShouldMove = true;
    }
    pawnInQuestion = null;
  }
}

