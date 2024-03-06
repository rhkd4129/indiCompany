package service.board;

import java.util.Map;

import dao.BoardDao;
import dto.BoardDto;
import util.CommandProcess1;
import util.ModelView;

public class NewAction implements CommandProcess1 {
	
	@Override
	public ModelView process(Map<String, String> paramMap) {
		Integer boardCode 		= null;
		BoardDao boardDao 		= null;
		BoardDto boardDto 		= null;
		
		
		boardCode =Integer.parseInt(paramMap.get("boardCode")); 
		boardDao = BoardDao.getInstance();
		boardDto = new BoardDto();
	    boardDto.setBoardCode(boardCode);
	    ModelView mv =new ModelView("boardList");
	    mv.getModel().put("boardDto",boardDto);
	    return mv;
		
	}

}
