-- Write your MySQL query statement below

/*
 * https://leetcode.com/problems/not-boring-movies/solutions/4533243/natural-laguage-solution/
 *
 * Rf :
 * https://leetcode.com/problems/not-boring-movies/solutions/4747136/100-beast-solution-easy-sql-query/
 */
select * from Cinema
where id % 2 = 1 and description != 'boring'
order by rating desc

/*
 * https://leetcode.com/problems/not-boring-movies/solutions/3839979/100-easy-fast-clean-solution/
 */
SELECT * FROM Cinema WHERE MOD( id, 2) = 1 AND 
description <> 'boring' ORDER BY rating DESC

/*
 * https://leetcode.com/problems/not-boring-movies/solutions/5011515/clean-easy-oracle-explanation/
 *
 * The asterisk `*` is a wildcard that represents all columns.
 *
 * `MOD(id, 2) <> 0`: This uses the MOD function to filter rows where the remainder of the 
 * division of the `id` column by `2` is not equal to `0`.
 *
 * `ORDER BY rating DESC`: This specifies the ordering of the results. It orders the selected 
 * rows by the rating column in descending order (DESC).
 */
SELECT * FROM Cinema
WHERE MOD(id, 2) <> 0 AND description <> 'boring'
ORDER BY rating DESC

/*
 * https://leetcode.com/problems/not-boring-movies/solutions/104483/my-solution/comments/165124
 */
select * from cinema c
where c.id mod 2 > 0 and c.description not in ('boring')
order by c.rating desc

/*
 * by myself
 *
 * https://leetcode.com/problems/not-boring-movies/solutions/1538748/sql-easy-solution/
 */
SELECT
  id, movie, description, rating
FROM
  Cinema
WHERE
  id % 2 = 1 AND description != 'boring'
ORDER BY
  rating DESC



