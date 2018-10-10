var global = this;

var console = {};
var process = {env:{NODE_ENV:"production"}};
console.debug = print;
console.warn = print;
console.log = print;
console.error = print;

/**
 * js-timeout-polyfill
 * @see https://blogs.oracle.com/nashorn/entry/setinterval_and_settimeout_javascript_functions
 */
(function (global) {
    'use strict';

    if (global.setTimeout ||
        global.clearTimeout ||
        global.setInterval ||
        global.clearInterval) {
        return;
    }

    var Timer = global.Java.type('java.util.Timer');

    function toCompatibleNumber(val) {
        switch (typeof val) {
            case 'number':
                break;
            case 'string':
                val = parseInt(val, 10);
                break;
            case 'boolean':
            case 'object':
                val = 0;
                break;

        }
        return val > 0 ? val : 0;
    }

    function setTimerRequest(handler, delay, interval, args) {
        handler = handler || function () {
        };
        delay = toCompatibleNumber(delay);
        interval = toCompatibleNumber(interval);

        var applyHandler = function () {
            handler.apply(this, args);
        };

        /*var runLater = function () {
         Platform.runLater(applyHandler);
         };*/

        var timer;
        if (interval > 0) {
            timer = new Timer('setIntervalRequest', true);
            timer.schedule(applyHandler, delay, interval);
        } else {
            timer = new Timer('setTimeoutRequest', false);
            timer.schedule(applyHandler, delay);
        }

        return timer;
    }

    function clearTimerRequest(timer) {
        timer.cancel();
    }

    /////////////////
    // Set polyfills
    /////////////////
    global.setInterval = function setInterval() {
        var args = Array.prototype.slice.call(arguments);
        var handler = args.shift();
        var ms = args.shift();

        return setTimerRequest(handler, ms, ms, args);
    };

    global.clearInterval = function clearInterval(timer) {
        clearTimerRequest(timer);
    };

    global.setTimeout = function setTimeout() {
        var args = Array.prototype.slice.call(arguments);
        var handler = args.shift();
        var ms = args.shift();

        return setTimerRequest(handler, ms, 0, args);
    };

    global.clearTimeout = function clearTimeout(timer) {
        clearTimerRequest(timer);
    };

    global.setImmediate = function setImmediate() {
        var args = Array.prototype.slice.call(arguments);
        var handler = args.shift();

        return setTimerRequest(handler, 0, 0, args);
    };

    global.clearImmediate = function clearImmediate(timer) {
        clearTimerRequest(timer);
    };

})(this);

//! Copyright 2012 Eric Wendelin - MIT License
(function(a){function b(a){var b=[],c=[],f=[],j=Object.is||function(d,a){return d===a?0!==d||1/d==1/a:d!=d&&a!=a},g=function(d){if(d!=d||0===d)for(var a=this.length;a--&&!j(this[a],d););else a=[].indexOf.call(this,d);return a},k=function(d,a){var b=0;return Object.create({},{next:{value:function(){if(b<d.items().length)switch(a){case "keys":return d.keys()[b++];case "values":return d.values()[b++];case "keys+values":return[].slice.call(d.items()[b++]);default:throw new TypeError("Invalid iterator type");
        }throw Error("Stop Iteration");}},iterator:{value:function(){return this}},toString:{value:function(){return"[object Map Iterator]"}}})},h=function(d,a){var e=g.call(c,d);-1<e?(b[e]=a,f[e]=a):(b.push([d,a]),c.push(d),f.push(a))},l=function(a){if(2!==a.length)throw new TypeError("Invalid iterable passed to Map constructor");h(a[0],a[1])};if(Array.isArray(a))a.forEach(l);else if(void 0!==a)throw new TypeError("Invalid Map");return Object.create(e,{items:{value:function(){return[].slice.call(b)}},keys:{value:function(){return[].slice.call(c)}},
    values:{value:function(){return[].slice.call(f)}},has:{value:function(a){return-1<g.call(c,a)}},get:{value:function(a){a=g.call(c,a);return-1<a?f[a]:void 0}},set:{value:h},size:{get:function(){return b.length}},clear:{value:function(){c.length=f.length=b.length=0}},"delete":{value:function(a){a=g.call(c,a);return-1<a?(c.splice(a,1),f.splice(a,1),b.splice(a,1),!0):!1}},forEach:{value:function(a,b){function c(){try{return e.next()}catch(a){}}if("function"!=typeof a)throw new TypeError("Invalid callback function given to forEach");
            for(var e=this.iterator(),f=c(),g=c();void 0!==f;)a.apply(b,[f[1],f[0],this]),f=g,g=c()}},iterator:{value:function(){return new k(this,"keys+values")}},toString:{value:function(){return"[Object Map]"}}})}var c=(a="undefined"==a)?this:global,a=a?{}:exports,e=b.prototype;b.prototype=e=b();c.Map=a.Map=c.Map||b}).call(this,typeof exports);
(function(){Map.prototype.filter=function(a){if("function"!=typeof a)throw new TypeError("Expected a function argument");var b=new Map;this.forEach(function(c,e,i){a(e,c,i)&&b.set(e,c)});return b};Map.prototype.merge=function(a){function b(a,b){c.set(b,a)}if(!(a instanceof Map))throw new TypeError("Cannot merge with objects that are not Maps");var c=new Map;this.forEach(b);a.forEach(b);return c};Map.prototype.fetch=function(a,b){return this.has(a)?this.get(a):b};Map.prototype.invert=function(){var a=
    new Map;this.forEach(a.set);return a};Map.prototype.reject=function(a){if("function"!=typeof a)throw new TypeError("Expected a function argument");this.forEach(function(b,c,e){if(a(c,b,e))e["delete"](c)}.bind(this))};Map.prototype.isEmpty=function(){return 0===this.keys().length}})();

// IE11 has an incomplete implementation of Set which doesn't allow you to iterate the keys
// so this code assumes you want a full implementation and will redefine Set if the half
// implementation is present
if (typeof Set === "undefined" || typeof Set.prototype.keys !== "function") {
    var Set = (function() {
        "use strict";

        var iterables = {
            "[object Array]": true,
            "[object Arguments]": true,
            "[object HTMLCollection]": true,
            "[object NodeList]": true
        };

        // shortcuts
        var hasOwn = Object.prototype.hasOwnProperty;
        var toString = Object.prototype.toString;

        function hasOwnProp(obj, prop) {
            return hasOwn.call(obj, prop);
        }

        function isIterable(item) {
            // for purposes of this implementation, an iterable is anything we can iterate with
            // a classic for loop:
            //     for (var i = 0; i < item.length; i++)
            // Currently accepts: array, arguments object, HTMLCollection, NodeList
            // and anything that has a .length with a numeric value and, if .length > 0, the first item has a nodeType property
            var name;
            if (typeof item === "object") {
                name = toString.call(item);
                return ((iterables[name] === true) ||
                    (typeof item.length === "number" &&
                        item.length >= 0 &&
                        (item.length === 0 || (typeof item[0] === "object" && item[0].nodeType > 0))
                    )
                );
            }
            return false;
        }

        // decide if we can use Object.defineProperty
        // include a test for Object.defineProperties (which IE8 does not have) to eliminate
        // using the broken Object.defineProperty in IE8
        var canDefineProperty = Object.defineProperty && Object.defineProperties;

        function setProperty(obj, propName, value, enumerable, writable) {
            if (canDefineProperty) {
                Object.defineProperty(obj, propName, {
                    enumerable: enumerable,
                    configurable: false,
                    writable: writable,
                    value: value
                });
            } else {
                obj[propName] = value;
            }
        }

        // this private function is used like a private method for setting
        // the .size property.  It cannot be called from outside this closure.
        var settable = false;
        function setSize(obj, val) {
            settable = true;
            obj.size = val;
            settable = false;
        }

        // this is the constructor function which will be returned
        // from this closure
        function SetConstructor(arg) {
            // private member variable, not used if IE8
            var size = 0;

            // set properties in cross-browser way
            setProperty(this, "baseType", "Set", false, false);   // not enumerable, not writeable
            setProperty(this, "_data", {}, false, true);          // not enumerable, writeable
            if (canDefineProperty) {
                Object.defineProperty(this, "size", {
                    enumerable: true,
                    configurable: false,
                    get: function() { return size;},
                    set: function(val) {
                        if (!settable) {throw new Error("Can't set size property on Set object.")}
                        size = val;
                    }
                });
            } else {
                // .size is just regular property in IE8
                this.size = 0;
            }
            // now add initial data
            // per spec make sure it isn't undefined or null
            if (arg !== undefined && arg !== null) {
                if (isIterable(arg)) {
                    for (var i = 0; i < arg.length; i++) {
                        this.add(arg[i]);
                    }
                    // also check our own custom property in case
                    // there is cross window code that won't pass instanceof
                } else if (arg instanceof Set || arg.baseType === "Set") {
                    arg.forEach(function(item) {
                        this.add(item);
                    }, this);
                }
            }
        }

        // state variables and shared constants
        var objectCntr = 0;
        var objectCntrBase = "obj_";
        var objectCntrProp = "__objectPolyFillID";

        // types where we just use the first 3 letters of the type
        // plus underscore + toString() to make the key
        // The first 3 letters of the type makes a namespace for each
        // type so we can have things like 0 and "0" as separate keys
        // "num_0" and "str_0".
        var autoTypes = {
            "string": true,
            "boolean": true,
            "number": true,
            "undefined": true
        };

        function getKey(val, putKeyOnObject) {
            // manufacture a namespaced key
            var type = typeof val, id;
            if (autoTypes[type]) {
                return type.substr(0, 3) + "_" + val;
            } else if (val === null) {
                return "nul_null";
            } else if (type === "object" || type === "function") {
                // coin a unique id for each object and store it on the object
                if (val[objectCntrProp]) {
                    return val[objectCntrProp];
                } else if (!putKeyOnObject) {
                    // it only returns null if there is no key already on the object
                    // and it wasn't requested to create a new key on the object
                    return null;
                } else {
                    // coin a unique id for the object
                    id = objectCntrBase + objectCntr++;
                    // include a test for Object.defineProperties to rule out IE8
                    // which can't use Object.defineProperty on normal JS objects
                    if (toString.call(val) === "[object Object]" && canDefineProperty) {
                        Object.defineProperty(val, objectCntrProp, {
                            enumerable: false,
                            configurable: false,
                            writable: false,
                            value: id
                        });
                    } else {
                        // no Object.defineProperty() or not plain object, so just assign property directly
                        val[objectCntrProp] = id;
                    }
                    return id;
                }
            } else {
                throw new Error("Unsupported type for Set.add()");
            }
        }

        function SetIterator(keys, data, format) {
            var index = 0, len = keys.length;
            this.next = function() {
                var val, result = {}, key;
                while (true) {
                    if (index < len) {
                        result.done = false;
                        key = keys[index++];
                        val = data[key];
                        // check to see if key might have been removed
                        // undefined is a valid value in the set so we have to check more than that
                        // if it is no longer in the set, get the next key
                        if (val === undefined && !hasOwnProp(data, key)) {
                            continue;
                        }
                        if (format === "keys") {
                            result.value = val;
                        } else if (format === "entries") {
                            result.value = [val, val];
                        }
                    } else {
                        // clear references to outside data
                        keys = null;
                        data = null;
                        result.done = true;
                    }
                    return result;
                }
            };
        }

        function getKeys(data) {
            var keys = [];
            for (var prop in data) {
                if (hasOwnProp(data, prop)) {
                    keys.push(prop);
                }
            }
            return keys;
        }

        SetConstructor.prototype = {
            add: function(val) {
                var key = getKey(val, true);
                if (!hasOwnProp(this._data, key)) {
                    this._data[key] = val;
                    setSize(this, this.size + 1);
                }
                return this;
            },
            clear: function() {
                this._data = {};
                setSize(this, 0);
            },
            // delete has to be in quotes for IE8 - go figure
            "delete": function(val) {
                var key = getKey(val, false);
                if (key !== null && hasOwnProp(this._data, key)) {
                    delete this._data[key];
                    setSize(this, this.size - 1);
                    return true;
                }
                return false;
            },
            // .remove() is non-standard, but here for anyone who wants to use it
            // so that you can use this polyfill all the way down to IE7 and IE8
            // since IE8 can't use a method named .delete()
            remove: function(val) {
                return this["delete"](val);
            },
            forEach: function(fn /*, context */) {
                // by spec, we have to type check the fn argument
                if (typeof fn !== "function") return;

                // context argument is optional, but .forEach.length is supposed to be 1 by spec
                // so we declare it this way
                var context = arguments[1];

                // forEach specifies that the iteration set is
                // determined before the first callback so we get all the
                // keys first
                var iter = this.keys(), next, item;
                while ((next = iter.next()) && !next.done) {
                    item = next.value;
                    fn.call(context, item, item, this);
                }
            },
            has: function(val) {
                var key = getKey(val, false);
                if (key === null) return false;
                return hasOwn.call(this._data, key);
            },
            values: function() {
                return this.keys();
            },
            keys: function() {
                return new SetIterator(getKeys(this._data), this._data, "keys");
            },
            entries: function() {
                return new SetIterator(getKeys(this._data), this._data, "entries");
            }
        };

        SetConstructor.prototype.constructor = SetConstructor;

        return SetConstructor;
    })();
}