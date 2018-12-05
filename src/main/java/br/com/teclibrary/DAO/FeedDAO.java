package br.com.teclibrary.DAO;

import br.com.teclibrary.entity.Feed;
import br.com.teclibrary.system.db.QueryType;
import br.com.teclibrary.system.impls.ModelPair;

import java.util.List;

public class FeedDAO extends BasicDAO<Feed, Integer> {

    public FeedDAO() {
        super(Feed.class);
    }

    public List<Feed> findFeedByUser(Integer ID) throws Exception {
        List<Feed> feedList = this.retrieveStatement("SELECT F FROM Feed F WHERE F.user.codigo = :P_CODUSU",
                QueryType.Normal_Query, new ModelPair("P_CODUSU", ID));
        return feedList;
    }
}
