-- Write your MySQL query statement below

/*
 * https://medium.com/jimmy-wang/sql-%E5%B8%B8%E7%94%A8%E8%AA%9E%E6%B3%95%E5%BD%99%E6%95%B4-%E5%9F%BA%E6%9C%AC%E9%81%8B%E7%AE%97-sql-003-3b771d4dacb8
 *
 * 看文氏圖彙整
 * 
 * Left Join ON A.key = B.key: 先出現的 table 全選
 *
 *     若只要先出現的, 加上 WHERE 後.key is null
 *
 * Right Join ON A.key = B.key: 後出現的 table 全選
 *
 *     若只要後出現的, 加上 WHERE 前.key is null
 *
 * Inner Join ON A.key = B.key: 取交集
 *
 * Full Outer Join ON A.key = B.key: 取聯集
 *
 *     若不要交集, 加上 WHERE A.key is null or B.key is null
 */

/*
 * by myself
 *
 * LEFT JOIN will put all values from Emplyees table, if corresponding row not exists 
 * in EmployeeUNI table, then a null value will be added.
 *
 * -------------------------------------------------------------------------------------
 *
 * We used left join because we wanted all rows from the Employee table. As Employee is 
 * written first means that its all rows will always be included.
 *
 * -------------------------------------------------------------------------------------
 *
 * Use left join so that output would contain every row from Employees table which in above 
 * code is considered as left table.You can also use right join but for that you should 
 * interchange position of Employees and EmployeeUNI in above code.
 *
 * -------------------------------------------------------------------------------------
 *
 * The "ON" clause specifies that the ID column in the Employees table should match the ID
 * column in the EmployeeUNI table. This will join the two tables based on the matching IDs,
 * allowing the query to retrieve the required data from both tables.
 *
 * Rf :
 * https://leetcode.com/problems/replace-employee-id-with-the-unique-identifier/solutions/3549131/sql-left-join-easy-solution/
 * https://leetcode.com/problems/replace-employee-id-with-the-unique-identifier/solutions/3549131/sql-left-join-easy-solution/comments/2129734
 * https://leetcode.com/problems/replace-employee-id-with-the-unique-identifier/solutions/3549131/sql-left-join-easy-solution/comments/2416928
 * https://leetcode.com/problems/replace-employee-id-with-the-unique-identifier/solutions/3549131/sql-left-join-easy-solution/comments/2219736
 * https://leetcode.com/problems/replace-employee-id-with-the-unique-identifier/solutions/3549131/sql-left-join-easy-solution/comments/2338783
 * https://leetcode.com/problems/replace-employee-id-with-the-unique-identifier/solutions/3361912/the-given-sql-query-selects-the-unique-id-and-name-of-employees-from-two-different-tables/
 */
SELECT
  EmployeeUNI.unique_id, Employees.name
FROM
  Employees
LEFT JOIN
  EmployeeUNI
ON
  Employees.id = EmployeeUNI.id

/*
 * https://leetcode.com/problems/replace-employee-id-with-the-unique-identifier/solutions/3782400/best-optimum-solution-with-explanation-using-joins/
 *
 * Approach
 *
 * 1. Imagine we have two tables: one for the employees and one for their unique 
 *    identifiers.
 * 2. We want to combine the information from both tables to get the unique identifier 
 *    and the name for each employee.
 * 3. To do this, we use a left join, which means we take all the employees from the 
 *    first table (Employees) and match them with their unique identifiers from the 
 *    second table (EmployeeUNI) based on their IDs.
 * 4. By performing this left join, we make sure that all the employees from the first 
 *    table are included in the result, even if they don't have a matching unique 
 *    identifier in the second table.
 * 5. The result of our query will be a combination of the unique identifier and the name 
 *    for each employee.
 *
 * Complexity
 *
 * Time complexity:
 *
 * The time complexity of the query depends on the number of rows in the Employees table 
 * and the EmployeeUNI table, as well as the efficiency of the join operation. Assuming
 * the join operation has been appropriately indexed, the time complexity is generally
 * O(n+m), where n is the number of rows in the Employees table and m is the number of
 * rows in the EmployeeUNI table.
 *
 * Space complexity:
 *
 * The space complexity depends on the result set produced by the query. The space
 * required to store the result set will depend on the number of matching rows between the
 * Employees table and the EmployeeUNI table. If the result set is significantly smaller
 * than the original tables, the space complexity can be considered relatively low.
 *
 * -------------------------------------------------------------------------------------
 *
 * Because we need all the rows of table Employees. If we use RIGHT in the same query, we
 * get ALL rows from EmployeeUNI and only 3 from Employee.
 *
 * But. We can switch tables in FROM clause and use RIGHT JOIN and get the same result.
 * For example:
 *
 * SELECT unique_id, name
 * FROM EmployeeUNI RIGHT JOIN Employees USING(id)
 *
 * Rf :
 * https://leetcode.com/problems/replace-employee-id-with-the-unique-identifier/solutions/3782400/best-optimum-solution-with-explanation-using-joins/comments/2212648
 */
SELECT eu.unique_id AS unique_id, e.name
FROM Employees e
LEFT JOIN EmployeeUNI eu USING(id)

/*
 * https://leetcode.com/problems/replace-employee-id-with-the-unique-identifier/solutions/3549131/sql-left-join-easy-solution/
 *
 * The second table was simply joined to the first table
 *
 * -------------------------------------------------------------------------------------
 *
 * To show the unique_id of each user, we perform LEFT JOIN with Employees on the Left Side.
 *
 * You can solve this using Right Join as well with Employees on the Right Side.
 *
 * When we perform LEFT JOIN with Employees on the left side, it tells the database to
 * include all the rows of Employee (table on left side) and only the intersecting or common
 * rows from EmployeeUNI (i.e. table on right side).
 *
 * -------------------------------------------------------------------------------------
 *
 * when joining tables, you want to specify from which tables you are selecting specific 
 * items. This makes your code more readable and also gets around the issue of selecting 
 * items that might have the same name in both tables. Aliasing the tables in your 
 * FROM and JOIN statements just makes it easier to quickly type out and read the code. 
 * Otherwise you'd have to write "Employees.id, EmployeeUNI.unique_id, etc."for every 
 * item you select
 *
 * -------------------------------------------------------------------------------------
 *
 * When we join table A with table B and both has same column name but different value 
 * how we going to know which column we are talking about.
 *
 * Using alias is useful when we want to join with same table name ( Self Join ).
 *
 * Some people use as alias_name other just alias_name for making alias.
 *
 * -------------------------------------------------------------------------------------
 *
 * These are nicknames for Employee and EmployeeUNI tables respectively. On the last line 
 * in the FROM section you can see that he designated Employees as "e" and 
 * EmployeeUNI as "eu" by declaring the e after Employees and eu After EmployeeUNI
 *
 * -------------------------------------------------------------------------------------
 *
 * If you remove 'AS', then your column names will not change with further use
 *
 * Rf : 
 * https://leetcode.com/problems/replace-employee-id-with-the-unique-identifier/solutions/3584620/explanation-of-how-to-use-join-correctly-mysql-pandas-beats-100/
 * https://leetcode.com/problems/replace-employee-id-with-the-unique-identifier/solutions/3549131/sql-left-join-easy-solution/comments/2033397
 * https://leetcode.com/problems/replace-employee-id-with-the-unique-identifier/solutions/3549131/sql-left-join-easy-solution/comments/1915246
 * https://leetcode.com/problems/replace-employee-id-with-the-unique-identifier/solutions/3549131/sql-left-join-easy-solution/comments/2172497
 * https://leetcode.com/problems/replace-employee-id-with-the-unique-identifier/solutions/3549131/sql-left-join-easy-solution/comments/1929087
 *
 * Others:
 * https://leetcode.com/problems/replace-employee-id-with-the-unique-identifier/solutions/3872822/pandas-vs-sql-elegant-short-all-30-days-of-pandas-solutions/
 */
select 
eu.unique_id as unique_id, e.name as name
from Employees e left join EmployeeUNI eu on e.id = eu.id

/*
 * https://leetcode.com/problems/replace-employee-id-with-the-unique-identifier/solutions/1203553/let-s-give-some-love-to-right-join/
 */
select eu.unique_id, e.name
from employeeuni eu
right join employees e on eu.id= e.id;

/*
 * https://leetcode.com/problems/replace-employee-id-with-the-unique-identifier/solutions/3361912/the-given-sql-query-selects-the-unique-id-and-name-of-employees-from-two-different-tables/
 *
 * The left join ensures that all the records from the Employees table are selected, along
 * with matching records from the EmployeeUNI table, if any. If there are no matching records
 * in the EmployeeUNI table, the result will still include the record from the Employees table,
 * with null values for the EmployeeUNI columns.
 *
 * The "ON" clause specifies that the ID column in the Employees table should match the ID
 * column in the EmployeeUNI table. This will join the two tables based on the matching IDs,
 * allowing the query to retrieve the required data from both tables.
 *
 * The "SELECT" statement specifies that the unique_id and name columns should be retrieved
 * from the resulting table. This will produce a table with two columns, one containing the
 * unique ID and the other containing the name of the employee.
 *
 */
select unique_id, name from Employees as e
left join EmployeeUNI as c
on e.id = c.id

/*
 * https://leetcode.com/problems/replace-employee-id-with-the-unique-identifier/solutions/553238/mysql-simple-solution/comments/780833
 *
 * Others:
 * https://leetcode.com/problems/replace-employee-id-with-the-unique-identifier/solutions/3862024/100-easy-fast-clean-2-solution/
 */
SELECT unique_id, name
FROM Employees
LEFT JOIN  EmployeeUNI
USING (id)

/*
 * https://leetcode.com/problems/replace-employee-id-with-the-unique-identifier/solutions/553238/mysql-simple-solution/comments/1623880
 */
SELECT unique_id, name
FROM Employees NATURAL LEFT JOIN EmployeeUNI

/*
 * https://leetcode.com/problems/replace-employee-id-with-the-unique-identifier/solutions/4795097/easiest-basic-sql-solution-3-approaches-beginner-level-to-advance/
 */
SELECT
    CASE
        WHEN EmployeeUNI.unique_id IS NULL THEN null
        ELSE EmployeeUNI.unique_id
    END AS unique_id,
    Employees.name
FROM Employees
LEFT JOIN EmployeeUNI ON Employees.id = EmployeeUNI.id;

/*
 * https://leetcode.com/problems/replace-employee-id-with-the-unique-identifier/solutions/4795097/easiest-basic-sql-solution-3-approaches-beginner-level-to-advance/
 */
SELECT COALESCE(EmployeeUNI.unique_id, null) AS unique_id, Employees.name
FROM Employees
LEFT JOIN EmployeeUNI ON Employees.id = EmployeeUNI.id;

/*
 * https://leetcode.com/problems/replace-employee-id-with-the-unique-identifier/solutions/553238/mysql-simple-solution/comments/1886591
 */
SELECT (SELECT unique_id FROM EmployeeUNI WHERE EmployeeUNI.id = Employees.id) as unique_id, name FROM Employees;
