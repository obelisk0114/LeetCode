-- Write your MySQL query statement below

/*
 * https://leetcode.com/problems/find-customer-referee/editorial/
 *
 * Approach: Using `<>`(`!=`) and `IS NULL`
 *
 * SELECT name FROM customer WHERE referee_Id <> 2;
 *
 * All the customers who were referred by nobody at all 
 * (NULL value in the referee_id column) don't show up.
 *
 * MySQL uses three-valued logic -- TRUE, FALSE and UNKNOWN. Anything compared to NULL 
 * evaluates to the third value: UNKNOWN. That "anything" includes NULL itself!
 * That's why MySQL provides the `IS NULL` and `IS NOT NULL` operators to specifically 
 * check for NULL.
 *
 * Thus, one more condition 'referee_id IS NULL' should be added to the WHERE clause 
 * as below.
 */
SELECT name FROM customer WHERE referee_id <> 2 OR referee_id IS NULL;

-- or

SELECT name FROM customer WHERE referee_id != 2 OR referee_id IS NULL;

/*
 * by myself
 *
 * You can improve runtime by using NOT IN instead of !=
 *
 * Rf :
 * https://leetcode.com/problems/find-customer-referee/solutions/3789317/easy-solution-mysql/comments/1985978
 */
SELECT
  name
FROM
  Customer
WHERE
  referee_id != 2 OR ISNULL(referee_id)

/*
 * https://leetcode.com/problems/find-customer-referee/solutions/2123225/4-different-solutions-for-same-query-most-efficient-solution/
 *
 * IFNULL(referee_id,0) <> 2;
 * Basically if the "referee_id" (first paramenter) is a NULL value the IFNULL function 
 * will return 0 (second paramenter):
 * "IFNULL(NULL, 0) <> 2" == "0 <> 2"
 */
SELECT
    name
FROM
    Customer
WHERE 
    IFNULL(referee_id,0) <> 2;

/*
 * https://leetcode.com/problems/find-customer-referee/solutions/2398637/simple-query-with-easy-null-handling-using-coalesce/
 *
 * Here COALESCE is used to replace NULL values with zero before checking whether 
 * it is equal to 2 or not.
 */
SELECT name
FROM Customer
WHERE COALESCE(referee_id,0) <> 2;

/*
 * https://leetcode.com/problems/find-customer-referee/solutions/2123225/4-different-solutions-for-same-query-most-efficient-solution/
 */
SELECT  
    name
FROM 
    Customer
WHERE 
    id NOT IN
        (
        SELECT id
        FROM Customer
        WHERE referee_id = 2
        )
