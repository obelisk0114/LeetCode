-- Write your MySQL query statement below

/*
 * https://leetcode.com/problems/recyclable-and-low-fat-products/editorial/
 */
SELECT
    product_id
FROM
    Products
WHERE
    low_fats = 'Y' AND recyclable = 'Y'


/*
 * by myself
 */
SELECT product_id
FROM Products
WHERE low_fats = 'Y'
AND recyclable = 'Y';
