#ifndef PIECE_H
#define PIECE_H

#include "board.h"
#include <string>

/**
 * @brief The Color enum is used to represent the color of a piece.
 *
 * Since you are free to choose wether an empty field is represented by a piece
 * object or simply a nullptr you are free to edit this enum if that is the case
 * in your implementeation.
 */
enum class Color
{
  White,
  Black,
  Empty
};

/**
 * @brief The Piece class represents a single chess piece
 *
 * This class is the base class of all other chess pieces. Every distinct chess
 * piece is implemented in it's own class. Header files for those classes are
 * not part of the supplied code, so you will have to write your own.
 * It is also possible to declare multiple classes in a single header, so you
 * could also declare the needed classes here.
 *
 * Every piece object has a distinct \ref displayString which is the string
 * representation of that particular piece. Subclasses of piece are supposed to
 * set those string via constructor chaining.
 */

class Piece
{
  public:
  /**
   * @brief Piece constructor
   * @param displayString The string representation of this piece
   * @param color The color of this piece
   */
  Piece(const std::string& displayString, Color color);

  /**
   * @brief Piece destructor
   *
   * This is needed in **every** inheritance hierarchy and insures that the
   * correct (subclass) destructor is always called when an object is deletet
   * via a base class pointer.
   *
   * You do not need to implement anything special here, it is just important
   * that the destructor is declared virtual.
   *
   * Setting the destructor to = default gives that destructor the default
   * behaviour (which is: do nothing)
   */
  virtual ~Piece() = default;

  /**
   * @brief isWhite return true if piece is White
   * @return true if piece is White
   */
  bool isWhite() const;

  /**
   * @brief isBlack return true if piece is Black
   * @return true if piece is Black
   */
  bool isBlack() const;

  /**
   * @brief isEmpty return true if this is an empty piece
   * @return true if piece is an empty piece
   *
   * Note: You can choose whether you want to represent the concept of an "empty
   * field" by a special type of object, or by having a nullptr for empty
   * fields.
   * When choosing the latter, you can omit this method.
   */
  bool isEmpty() const;

  /**
   * @brief toString returns string representation of piece
   * @return string representation pf piece
   */
  std::string toString() const;

  /**
   * @brief isMovePossible checks if the supplied move is possible
   * @return true if the move was possible
   *
   * This method need to be virtual because it is overwritten in certain
   * subclasses. You can implement a "default version" in the piece class which
   * just return true.
   * The "virtual" keyword has the effect, that, for example,
   * Rook::isMovePossible is called automagically when called on a piece* when
   * that piece* is referencing a Rook object.
   */
  virtual bool isMovePossible(const Board& b, const Board::Move& mov){
      b.getPiece(mov.from.col,mov.from.row);
      return true;
  }

  private:
  const std::string displayString;
  const Color color;

};


#endif // PIECE_H
