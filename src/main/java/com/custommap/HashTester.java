package com.custommap;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class HashTester {

    private static class Test {
        String desc;
        List<SubTest> subTests = new LinkedList<SubTest>();
        float weight;
        float score;

        public Test(String desc, float weight) {
            this.desc = desc;
            this.weight = weight;
        }

        public void addSubTest(SubTest subTest) {
            subTests.add(subTest);
        }

        public void run() {
            int passed = 0;
            System.out.printf("%s (%.1fp)%n", desc, weight);

            for (SubTest subTest : subTests) {
                System.out.println("\t" + subTest);
                if (subTest.status)
                    passed++;
            }

            score = weight * passed / subTests.size();

            System.out.printf("%n\tPassed: %d/%d -> %d%%%n\tScore: %.2f%n%n",
                    passed,
                    subTests.size(),
                    100 * passed / subTests.size(),
                    score);
        }
    }

    private static class SubTest {
        public String desc;
        public boolean status;

        public SubTest(String desc, boolean status) {
            this.desc = desc;
            this.status = status;
        }

        @Override
        public String toString() {
            final int ELLIPSIS = 60;
            StringBuffer buf = new StringBuffer();

            buf.append(desc);
            for (int i = 0; i < ELLIPSIS - desc.length(); i++)
                buf.append(".");

            buf.append(status ? "OK" : "X");

            return buf.toString();
        }
    }

    private List<String> getRandomStrings(int count) {
        Random r = new Random();
        StringBuffer buf = new StringBuffer();
        List<String> strings = new LinkedList<String>();

        for (int s = 0; s < count; s++) {
            buf.delete(0, buf.length());
            int length = r.nextInt(10) + 1; // avoid 0 length strings

            for (int i = 0; i < length; i++) {
                char c = (char) (r.nextInt(93) + 33);
                buf.append(c);
            }

            strings.add(buf.toString());
        }

        return strings;
    }

    private int translate(int hashCode, int size) {
        return Math.abs(hashCode) % size;
    }

    public void runBasicTests() {
        List<Test> tests = new LinkedList<Test>();
        Test test;
        CustomMapInterface<String, Integer> map;
        Integer x;
        final int NUM_BUCKETS = 16;

        // Test 1 - Empty Hash
        test = new Test("Test 1 - Empty Hash", 1.0f);
        map = new CustomMapImpl<>(NUM_BUCKETS);

        test.addSubTest(new SubTest("get(\"abc\") == null", map.get("abc") == null));
        test.addSubTest(new SubTest("remove(\"abc\") == null", map.remove("abc") == null));
        test.addSubTest(new SubTest("size == 0", map.size() == 0));
        test.addSubTest(new SubTest("numBuckets == " + NUM_BUCKETS, map.getBuckets().size() == NUM_BUCKETS));

        tests.add(test);

        // Test 2 - Multiple put
        test = new Test("Test 2 - Multiple put", 1.0f);
        map = new CustomMapImpl<>(NUM_BUCKETS);

        test.addSubTest(new SubTest("put(\"abc\", 5) == null", map.put("abc", 5) == null));
        test.addSubTest(new SubTest("get(\"abc\") == 5", (x = map.get("abc")) != null && x == 5));
        test.addSubTest(new SubTest("size == 1", map.size() == 1));
        test.addSubTest(new SubTest("put(\"abc\", 7) == 5", (x = map.put("abc", 7)) != null && x == 5));
        test.addSubTest(new SubTest("get(\"abc\") == 7", (x = map.get("abc")) != null && x == 7));
        test.addSubTest(new SubTest("size == 1", map.size() == 1));
        test.addSubTest(new SubTest("put(\"xyz\", 11) == null", map.put("xyz", 11) == null));
        test.addSubTest(new SubTest("get(\"xyz\") == 11", (x = map.get("xyz")) != null && x == 11));
        test.addSubTest(new SubTest("size == 2", map.size() == 2));

        tests.add(test);

        // Test 3 - Multiple remove
        test = new Test("Test 3 - Multiple remove", 1.0f);
        map = new CustomMapImpl<>(NUM_BUCKETS);

        test.addSubTest(new SubTest("put(\"abc\", 5) == null", map.put("abc", 5) == null));
        test.addSubTest(new SubTest("remove(\"abc\") == 5", (x = map.remove("abc")) != null && x == 5));
        test.addSubTest(new SubTest("size == 0", map.size() == 0));
        test.addSubTest(new SubTest("remove(\"abc\") == null", map.remove("abc") == null));
        test.addSubTest(new SubTest("size == 0", map.size() == 0));

        tests.add(test);

        // Test 4 - Correct dispersion
        test = new Test("Test 4 - Correct dispersion", 2.0f);
        map = new CustomMapImpl<>(NUM_BUCKETS);

        List<String> randomStrings = getRandomStrings(1000);
        for (String randomString : randomStrings)
            map.put(randomString, 1);
        boolean status = isStatus(map, NUM_BUCKETS);

        test.addSubTest(new SubTest("Verificare", status));

        tests.add(test);

        float score = 0;
        for (Test t : tests) {
            t.run();
            score += t.score;
        }
        System.out.printf("TOTAL: %.2f%n", score);

    }

    private boolean isStatus(CustomMapInterface<String, Integer> map, int NUM_BUCKETS) {
        List<? extends CustomMapInterface.Bucket<String, Integer>> buckets = map.getBuckets();

        int crtBucket = 0;
        boolean status = true;

        for (CustomMapInterface.Bucket<String, Integer> bucket : buckets) {
            List<? extends CustomMapInterface.Entry<String, Integer>> entries = bucket.getEntries();

            for (CustomMapInterface.Entry<String, Integer> entry : entries) {
                if (translate(entry.getKey().hashCode(), NUM_BUCKETS) != crtBucket) {
                    status = false;
                    break;
                }
            }

            crtBucket++;
        }
        return status;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        HashTester tester = new HashTester();

        tester.runBasicTests();
    }

}
