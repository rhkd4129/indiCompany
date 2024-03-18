package service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dao.BoardDao;
import dto.BoardDto;
import util.servletUtils.ServletRequestMapper;

public class BoardService {
    private static final BoardService instance = new BoardService();
    private static final Logger logger = LoggerFactory.getLogger(BoardService.class);

    private BoardService() {}

    public static BoardService getInstance() {
        return instance;
    }

    public void listBoard(Map<String, String> paramMap, Map<String, Object> model) throws SQLException, NullPointerException, Exception {
        
            BoardDao boardDao = BoardDao.getInstance();
            List<BoardDto> boardList = boardDao.listBoard(new BoardDto());
            model.put("boardList", boardList);
    }

    public void contentBoard(Map<String, String> paramMap, Map<String, Object> model) throws SQLException, NullPointerException, Exception {
            BoardDao boardDao = BoardDao.getInstance();            
            BoardDto boardDto = ServletRequestMapper.convertMapToDto(paramMap, BoardDto.class);
            BoardDto resultBoardDto = boardDao.selectBoard(boardDto);
            model.put("board", resultBoardDto);
    }

        
        
    public void insertFormBoard(Map<String, String> paramMap, Map<String, Object> model)  {}

    
    public void insertBoard(Map<String, String> paramMap, Map<String, Object> model) throws SQLException, NullPointerException, Exception {    		
            BoardDao boardDao = BoardDao.getInstance();
            BoardDto boardDto = ServletRequestMapper.convertMapToDto(paramMap, BoardDto.class);
            int result = boardDao.insertBoard(boardDto);
            model.put("result", result);
    }

    public void updateFormBoard(Map<String, String> paramMap, Map<String, Object> model) throws SQLException, NullPointerException, Exception {
            BoardDao boardDao = BoardDao.getInstance();
            BoardDto boardDto = ServletRequestMapper.convertMapToDto(paramMap, BoardDto.class);
            BoardDto resultBoardDto = boardDao.selectBoard(boardDto);           
            model.put("board", resultBoardDto);
    }

    public void updateBoard(Map<String, String> paramMap, Map<String, Object> model) throws SQLException, NullPointerException, Exception {
            BoardDao boardDao = BoardDao.getInstance();
            BoardDto boardDto = ServletRequestMapper.convertMapToDto(paramMap, BoardDto.class);
            int result = boardDao.updateBoard(boardDto);
            model.put("result", result);
    }

    public void deleteBoard(Map<String, String> paramMap, Map<String, Object> model) throws SQLException, NullPointerException, Exception {
            BoardDao boardDao = BoardDao.getInstance();
            BoardDto boardDto = ServletRequestMapper.convertMapToDto(paramMap, BoardDto.class);
            int result = boardDao.deleteBoard(boardDto);
            model.put("result", result);
      
    }
}

