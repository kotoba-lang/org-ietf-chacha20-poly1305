#!/usr/bin/env nbb
;; Run the suite on the ClojureScript side.
;;
;; Not a formality. ChaCha20 is 32-bit unsigned words, and JavaScript's
;; bitwise operators return a SIGNED 32-bit result while the JVM's operate on
;; 64-bit longs that do not truncate — neither is the word the specification
;; describes, which is why every operation in `chacha20.word` ends in `u32`.
;; Poly1305's limbs are ten bits rather than the usual twenty-six precisely
;; so the intermediates stay exact here, where a number stops being an exact
;; integer at 2^53.
;;
;;   nbb --classpath "$(clojure -A:cljs -Spath)" scripts/verify-cljs.cljs
(ns verify-cljs
  (:require [clojure.test :as t]
            [chacha20.core-test]))

(defmethod t/report [:cljs.test/default :end-run-tests] [m]
  (println)
  (if (t/successful? m)
    (println "all checks passed on the ClojureScript path")
    (do (println "FAILED on the ClojureScript path")
        (js/process.exit 1))))

(t/run-tests 'chacha20.core-test)
