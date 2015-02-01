SELECT 
	m.file_id AS 'id', 
	m.url, 
	m.title, 
	m.order_in_collection AS '#', 
	m.collection, 
	m.subset, 
	m.set, 
	m.artist, 
	sf.name AS 'subform', 
	f.name  AS  'form', 
	sg.name AS 'subgenre', 
	g.name  AS  'genre', 
	c.name  AS  'culture', 
	e.name  AS  'era', 
	t.name  AS  'tradition'
FROM 
	midi_files m
	LEFT JOIN subforms sf  ON m.subform_id   = sf.subform_id
	LEFT JOIN forms f      ON m.form_id      = f.form_id
	LEFT JOIN subgenres sg ON m.subgenre_id  = sg.subgenre_id
	LEFT JOIN genres g     ON m.genre_id     = g.genre_id
	LEFT JOIN cultures c   ON m.culture_id   = c.culture_id
	LEFT JOIN eras e       ON m.era_id       = e.era_id
	LEFT JOIN traditions t ON m.tradition_id = t.tradition_id
ORDER BY
	m.file_id;