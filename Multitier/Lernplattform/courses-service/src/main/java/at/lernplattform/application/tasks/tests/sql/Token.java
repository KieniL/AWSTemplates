package at.lernplattform.application.tasks.tests.sql;

public enum Token {
	SELECT,
	DISTINCT,
	TOP,
	INTO,
	FROM,
	WHERE,
	JOIN, 
	ON,
	GROUP_BY,
	HAVING,
	ORDER_BY,
	OPERATOR, // =; >; >=; <; >=; <>; !=; LIKE; IN; IS NULL; IS TRUE; IS FALSE;
	BETWEEN,
	AS,
	CASE,
	WHEN,
	THEN,
	ELSE,
	END,
	FUNCTION, // coalesce; nullif; ...
	BOOL_OPERATORS, //AND, OR
	COMMENT,
	COMMENTSTART,
	COMMENTEND,
	UNION,
	UNION_ALL,
	STRING,
	ERROR
}
