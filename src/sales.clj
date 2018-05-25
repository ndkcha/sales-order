(ns sales
  (:gen-class)
  (:require [clojure.string :as str]))

(def choice)
(def customers)
(def customer)
(def products)
(def product)
(def sales)
(def sale)
(def noOfItems)
(def query)
(def customerName)
(def productName)

(defn toNum [s]
  (if (re-find #"^-?\d+\.?\d*$" s)
    (read-string s)))

(defn SearchFn [i q items]
  (def noOfItems (alength (to-array items)))
  (when (< i noOfItems)
    (if (str/starts-with? (nth items i) q)
      (nth items i)
      (recur (inc i) q items))))

(defn SearchByNameFn [i q items]
  (def noOfItems (alength (to-array items)))
  (def query (str/lower-case q))
  (when (< i noOfItems)
    (if (= (str/lower-case (nth (clojure.string/split (nth items i) #"[|]+") 1)) query)
      (nth items i)
      (recur (inc i) q items))))

(defn FindPriceFn [i productId products]
  (def noOfItems (alength (to-array products)))
  (when (< i noOfItems)
    (if (str/starts-with? (nth products i) productId)
      (toNum (nth (clojure.string/split (nth products i) #"[|]+") 2))
      (recur (inc i) productId products))))


(defn CountSalesFn [i c customerId sales products]
  (if (= i (alength (to-array sales)))
    c
    (when (< i (alength (to-array sales)))
      (def sale (clojure.string/split (nth sales i) #"[|]+"))
      (if (= (nth sale 1) (str customerId))
        (recur (inc i) (+ c (* (FindPriceFn 0 (nth sale 2) products) (toNum (nth sale 3)))) customerId sales products)
        (recur (inc i) c customerId sales products)))))

(defn CountStockFn [i c productId sales]
  (if (= i (alength (to-array sales)))
    c
    (when (< i (alength (to-array sales)))
      (def sale (clojure.string/split (nth sales i) #"[|]+"))
      (if (= (nth sale 2) (str productId))
        (recur (inc i) (+ c (toNum (nth sale 3))) productId sales)
        (recur (inc i) c productId sales)))))

(defn DisplayCustomerTable [customers]
  (dotimes [n (alength (to-array customers))]
    (def customer (clojure.string/split (nth customers n) #"[|]+"))
    (print (nth customer 0) ": [" (nth customer 1) "," (nth customer 2) "," (nth customer 3) "]\n")))

(defn DisplayProductTable [products]
  (dotimes [n (alength (to-array products))]
    (def product (clojure.string/split (nth products n) #"[|]+"))
    (print (nth product 0) ": [" (nth product 1) "," (nth product 2) "]\n")))

(defn DisplaySalesTable [customers products sales]
  (dotimes [n (alength (to-array sales))]
    (def sale (clojure.string/split (nth sales n) #"[|]+"))
    (def customer (SearchFn 0 (nth sale 1) customers))
    (def product (SearchFn 0 (nth sale 2) products))
    (print (nth sale 0) ": [ ")
    (print (nth (clojure.string/split customer #"[|]+") 1) ", ")
    (print (nth (clojure.string/split product #"[|]+") 1) ", ")
    (print (nth sale 3) "]\n")))

(defn TotalSalesCustomer [customers products sales]
  (println "Enter the name: ")
  (def customerName (read-line))
  (def customer (SearchByNameFn 0 customerName customers))
  (if customer
    (println (nth (clojure.string/split customer #"[|]+") 1) ":" (CountSalesFn 0 0.0 (nth customer 0) sales products))
    (println "Customer not found!")))

(defn TotalCountProduct [products sales]
  (println "Enter the name: ")
  (def productName (read-line))
  (def product (SearchByNameFn 0 productName products))
  (if product
    (println (nth (clojure.string/split product #"[|]+") 1) ":" (CountStockFn 0 0 (nth product 0) sales))
    (println ("Product not found!"))))

(defn ExitApp []
  (println "Good bye!"))

(defn DisplayMenu []
  (def customers (clojure.string/split-lines (slurp "cust.txt")))
  (def products (clojure.string/split-lines (slurp "prod.txt")))
  (def sales (clojure.string/split-lines (slurp "sales.txt")))
  (println "1. Display Customer Table")
  (println "2. Display Product Table")
  (println "3. Display Sales Table")
  (println "4. Total Sales for Customer")
  (println "5. Total Count for Product")
  (println "6. Exit")
  (println "Enter your choice: ")
  (def choice (read-line))
  (case choice "1" (DisplayCustomerTable customers)
               "2" (DisplayProductTable products)
               "3" (DisplaySalesTable customers products sales)
               "4" (TotalSalesCustomer customers products sales)
               "5" (TotalCountProduct products sales)
               "6" (ExitApp))
  (if (not= choice "6")
    (recur)))

(DisplayMenu)
