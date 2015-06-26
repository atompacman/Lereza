SELECT name FROM %1s WHERE parent_%2_id = (
	SELECT %2_id FROM %2s WHERE name = '%3'
);