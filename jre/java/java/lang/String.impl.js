/**
 * Impl super source for java.lang.String.
 */
goog.module('gen.java.lang.String$impl');


let Object = goog.require('gen.java.lang.Object$impl');
let $Util = goog.require('nativebootstrap.Util$impl');

let Class = goog.forwardDeclare('gen.java.lang.Class$impl');


class String extends Object {
  /**
   * Returns whether the provided instance is an instance of this class.
   * @return {boolean}
   * @public
   * @nocollapse
   */
  static $isInstance(instance) { return typeof instance == 'string'; }

  /**
   * Returns whether the provided class is or extends this class.
   * @param {Function} classConstructor
   * @return {boolean}
   * @public
   * @nocollapse
   */
  static $isAssignableFrom(classConstructor) {
    return $Util.$canCastClass(classConstructor, String);
  }

  /**
   * @return {Class}
   * @public
   * @nocollapse
   */
  static $getClass() {
    String.$clinit();
    if (!String.$classString) {
      String.$classString = Class.$createForClass(
        $Util.$generateId('String'),
        $Util.$generateId('java.lang.String'),
        Object.$getClass(),
        $Util.$generateId('java.lang.String'));
    }
    return String.$classString;
  }

  /**
   * Runs inline static field initializers.
   * @protected
   * @nocollapse
   */
  static $clinit() {
    Class = goog.module.get('gen.java.lang.Class$impl');
    Object.$clinit();
  }
};


/**
 * Export class.
 */
exports = String;