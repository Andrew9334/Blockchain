//package com.blockchaine.blockchane.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.p2p.solanaj.core.PublicKey;
//import org.p2p.solanaj.rpc.RpcApi;
//import org.p2p.solanaj.rpc.RpcClient;
//import org.p2p.solanaj.rpc.RpcException;
//import org.p2p.solanaj.rpc.types.TokenResultObjects;
//
//import java.lang.reflect.Field;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//public class SolanaServiceTest {
//
//    @Mock
//    private RpcClient rpcClient;
//
//    @Mock
//    private RpcApi rpcApi;
//
//    @InjectMocks
//    private SolanaService solanaService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        when(rpcClient.getApi()).thenReturn(rpcApi);
//    }
//
//    @Test
//    void testGetBalance() throws RpcException {
//        String walletAddress = "5Ey6RzCWWXaM32kY7Pc8h7GHi92YvEzFZpV3WJJdHJ7r";
//        long mockBalance = 1_000_000_000L; // 1 SOL в Lamports
//
//        PublicKey mockPublicKey = new PublicKey(walletAddress);
//
//        when(rpcApi.getBalance(eq(mockPublicKey))).thenReturn(mockBalance);
//
//        long balance = solanaService.getBalance(walletAddress);
//
//        assertEquals(mockBalance, balance);
//
//        verify(rpcApi, times(1)).getBalance(eq(mockPublicKey));
//    }
//}
