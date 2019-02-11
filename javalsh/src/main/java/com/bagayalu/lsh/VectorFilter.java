package com.weibo.ml.antispam.lsh;

import java.util.ArrayList;
import java.util.Set;

public interface VectorFilter {

    ArrayList<Candidate> filterVectors(ArrayList<Candidate> list);
}
