package com.example.gobang;

public interface GobangCallback {
    void Gameover(int winner);
    void ChangeGamer(boolean isWhite );
}
