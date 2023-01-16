package com.postype.sns.domain.member.model.util;

import java.util.List;

public record PageCursor<T> (
	CursorRequest nextCursorRequest,
	List<T> contents
){

}
