package app.ethereum;


import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;

public class Main {

	 static Web3j web3j;
	 static String DEFAULT_ADDRESS = "";
	 
	 public static void main(String[] args) {
		 
		  web3j = Web3j.build(new HttpService("HTTP://127.0.0.1:7545"));
		  EthBlockNumber ethBlockNumber = getBlockNumber();
		  System.out.println("ethBlockNumber : "+ethBlockNumber.getBlockNumber());
		 EthAccounts ethAccounts =  getEthAccounts();
		 System.out.println("ethAccounts : "+ethAccounts.getAccounts());
		 DEFAULT_ADDRESS = ethAccounts.getAccounts().get(0);
		 EthGetTransactionCount ethGetTransactionCount = getTransactionCount();
		 System.out.println("ethGetTransactionCount : "+ethGetTransactionCount.getRawResponse());
		 EthGetBalance ethGetBalance = getEthBalance();
		 System.out.println("ethGetBalance : "+ethGetBalance.getResult());
	 }
	 
	
	 
	 public static EthBlockNumber getBlockNumber() {
		 
		    EthBlockNumber result = new EthBlockNumber();
		    try {
				result = web3j.ethBlockNumber()
				  .sendAsync()
				  .get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    return result;
		}

	 public static EthAccounts getEthAccounts() {
		 
		    EthAccounts result = new EthAccounts();
		    try {
				result = web3j.ethAccounts()
				    .sendAsync() 
				    .get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    return result;
	}
	 
	 public static EthGetTransactionCount getTransactionCount() {
		    EthGetTransactionCount result = new EthGetTransactionCount();
		    try {
				result = web3j.ethGetTransactionCount(DEFAULT_ADDRESS, 
				  DefaultBlockParameter.valueOf("latest"))
				    .sendAsync() 
				    .get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    return result;
	}
	 
	 public static EthGetBalance getEthBalance() {
		    EthGetBalance result = new EthGetBalance();
		    try {
				 web3j.ethGetBalance(DEFAULT_ADDRESS, 
				  DefaultBlockParameter.valueOf("latest"))
				    .sendAsync() 
				    .get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    return result;
		}
}
