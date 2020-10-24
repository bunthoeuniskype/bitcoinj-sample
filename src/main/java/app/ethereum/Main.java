package app.ethereum;


import java.io.IOException;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;

public class Main {

	 public static void main(String[] args) {
		 connectEthereum();
	 }
	 
	 public static void connectEthereum() {
		 
		 System.out.println("Connecting to Ethereum ...");
		    Web3j web3 = Web3j.build(new HttpService("http://localhost:8545"));
		    System.out.println("Successfuly connected to Ethereum");

		    try {
		      // web3_clientVersion returns the current client version.
		      Web3ClientVersion clientVersion = web3.web3ClientVersion().send();

		      // eth_blockNumber returns the number of most recent block.
		      EthBlockNumber blockNumber = web3.ethBlockNumber().send();

		      // eth_gasPrice, returns the current price per gas in wei.
		      EthGasPrice gasPrice = web3.ethGasPrice().send();

		      // Print result
		      System.out.println("Client version: " + clientVersion.getWeb3ClientVersion());
		      System.out.println("Block number: " + blockNumber.getBlockNumber());
		      System.out.println("Gas price: " + gasPrice.getGasPrice());

		    } catch (IOException ex) {
		      throw new RuntimeException("Error whilst sending json-rpc requests", ex);
		    }
	 }

}
