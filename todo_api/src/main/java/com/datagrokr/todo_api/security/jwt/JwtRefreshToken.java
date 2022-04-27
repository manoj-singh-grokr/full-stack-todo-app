package com.datagrokr.todo_api.security.jwt;

public class JwtRefreshToken
{
    private String refreshToken;

    public String getRefreshToken()
    {
        return refreshToken;
    }

    public String getSubject()
    {
        return JwtUtils.getSubjectFromToken(this.refreshToken);
    }
}
