package algo.ggap.detector;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BaseDetectorTest {
    @Test
    public void testBranches() {
        BaseDetector.Result result = new BaseDetector.Result();
        BaseDetector.Branch branch = result.addNewBranch();
        branch.add(0, 1);
        branch.add(2, 3);
        result.addNewBranch().add(0, 2);
        result.addNewBranch().add(0, 3);

        assertEquals(result.branches.size(), 3);
        assertEquals(result.branches.get(0).edges.size(), 2);
    }


}