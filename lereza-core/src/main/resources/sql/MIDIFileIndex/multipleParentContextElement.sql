SELECT name FROM %1s WHERE %1_id IN (
	SELECT %1_id FROM %1_%2_matches WHERE parent_%2_id = (
		SELECT %2_id FROM %2s WHERE name = '%3'
	)
);