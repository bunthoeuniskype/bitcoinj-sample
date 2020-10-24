package app;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.google.bitcoin.core.*;
import com.google.bitcoin.core.Wallet.SendRequest;
import com.google.bitcoin.store.BlockStore;
import com.google.bitcoin.store.BlockStoreException;
import com.google.bitcoin.store.MemoryBlockStore;
import com.google.bitcoin.store.UnreadableWalletException;

public class SendCoins {

	// command line java SendCoins test test.wallet 0.00001 msw5y3txDXEnBG6zhbCe8HMAmSspzA8BcV
	public static void main(String[] args) throws InsufficientMoneyException, PeerException, IOException, UnreadableWalletException, AddressFormatException, BlockStoreException {

		if (args.length != 4) {
			System.out.println("Usage: java SendCoins prod|test wallet amount recipient");
			System.exit(1);
		}

		// we get the following from the command line ...
		// (this is not secure - needs validation)
		String network = args[0]; // "test" or "prod"
		String walletFileName = args[1]; // wallet file name
		String amountToSend = args[2]; // milli-BTC
		String recipient = args[3]; // Bitcoin address

		// the Bitcoin network to use
		final NetworkParameters netParams;
		// check for production Bitcoin network ...
		if (network.equalsIgnoreCase("prod")) {
			netParams = NetworkParameters.prodNet();
			// ... otherwise use the testnet
		} else {
			netParams = NetworkParameters.testNet();
		}

		// data structure for block chain storage
		BlockStore blockStore = new MemoryBlockStore(netParams);
		// declare object to store and understand block chain
		BlockChain chain;
		// declare wallet
		Wallet wallet;

		try {
			// wallet file that contains Bitcoins we can send
			final File walletFile = new File(walletFileName);
			// load wallet from file
			wallet = Wallet.loadFromFile(walletFile);

			// how man milli-Bitcoins to send
			BigInteger btcToSend = new BigInteger(amountToSend);

			// initialize BlockChain object
			chain = new BlockChain(netParams, wallet, blockStore);

			// instantiate Peer object to handle connections
			final Peer peer = new Peer(netParams, new VersionMessage(netParams, 1), chain,
					new PeerAddress(InetAddress.getLocalHost()));

			// connect to peer node on localhost
			peer.connectionOpened();

			// recipient address provided by official Bitcoin client
			Address recipientAddress = new Address(netParams, recipient);

			// tell peer to send amountToSend to recipientAddress
			Transaction sendTxn = wallet.sendCoins(peer, SendRequest.to(recipientAddress, btcToSend));
			// null means we didn't have enough Bitcoins in our wallet for the transaction
			if (sendTxn == null) {
				System.out.println("Cannot send requested amount of " + Utils.bitcoinValueToFriendlyString(btcToSend)
						+ " BTC; wallet only contains " + Utils.bitcoinValueToFriendlyString(wallet.getBalance())
						+ " BTC.");
			} else {
				// once communicated to the network (via our local peer),
				// the transaction will appear on Bitcoin explorer sooner or later
				System.out.println(Utils.bitcoinValueToFriendlyString(btcToSend)
						+ " BTC sent. You can monitor the transaction here:\n" + sendTxn.getHashAsString());
			}
			// save wallet with new transaction(s)
			wallet.saveToFile(walletFile);
			// handle the various exceptions; this needs more work
	
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

}
