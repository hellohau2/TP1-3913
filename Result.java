class Result {
    String filePath;
    int tlocResult;
    int tassertResult;

    Result(String filePath, int tlocResult, int tassertResult) {
        this.filePath = filePath;
        this.tlocResult = tlocResult;
        this.tassertResult = tassertResult;
    }
}