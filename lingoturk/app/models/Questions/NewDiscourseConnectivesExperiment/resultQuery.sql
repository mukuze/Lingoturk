SELECT NewDiscourseConnectivesResults.*, NewDiscourseConnectives_relation, NewDiscourseConnectives_nr, NewDiscourseConnectives_WSJID, NewDiscourseConnectives_pdtbSense, NewDiscourseConnectives_rstSense
FROM
	NewDiscourseConnectivesResults
JOIN
	Questions
USING (questionId)
ORDER BY id;