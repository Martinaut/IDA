import { Injectable } from '@angular/core';
import { Client, IFrame, IMessage, StompSubscription } from '@stomp/stompjs';
import { Observable, Subject } from 'rxjs';

/**
 * A service that manages the websocket connection.
 */
@Injectable({
  providedIn: 'root'
})
export class ConnectionService {

  private initializedStateChangedSource = new Subject<boolean>();
  private connectionStateChangedSource = new Subject<boolean>();
  private resultMessageReceivedSource = new Subject<string>();
  private asMessageReceivedSource = new Subject<string>();

  private client: Client;
  private resultSub: StompSubscription;
  private asSub: StompSubscription;
  private initialData: any;

  /**
   * Initializes a new instance of class ConnectionService.
   */
  constructor() {
    this.client = null;
    this.initialData = {
      locale: 'en',
      initialSentence: null
    };
  }

  // region --- Events ---
  /**
   * This events gets emitted when the connection status changed.
   * true = connected, false = disconnected
   */
  get connectionStateChanged(): Observable<boolean> {
    return this.connectionStateChangedSource.asObservable();
  }

  /**
   * This events gets emitted when the initialization status of the websocket client changed.
   * true = initialized, false = not initialized
   */
  get initializedStateChanged(): Observable<boolean> {
    return this.initializedStateChangedSource.asObservable();
  }

  /**
   * This events get emitted when a result message has been received.
   * The event contains the message body.
   */
  get resultMessageReceived(): Observable<string> {
    return this.resultMessageReceivedSource.asObservable();
  }

  /**
   * This events get emitted when an analysis situation message has been received.
   * The event contains the message body.
   */
  get asMessageReceived(): Observable<string> {
    return this.asMessageReceivedSource.asObservable();
  }

  // endregion

  /**
   * Sends a message to the server.
   * If the client is initialized but not connected, the method sends the start message instead of the user input message.
   *
   * @param input The user input.
   */
  sendMessage(input: string): void {
    if (this.client == null) {
      throw new Error('Client not initialized.');
    }
    if (!this.isConnected()) {
      this.initialData.initialSentence = input;
      this.client.activate();
    } else {
      const bodyData = JSON.stringify({userInput: input});
      console.log('>>> SEND MESSAGE \r\n' + bodyData);

      this.client.publish({
        destination: '/app/input',
        body: bodyData
      });
    }
  }

  /**
   * Returns whether the client is initialized and ready to connect.
   */
  isInitialized(): boolean {
    return this.client != null;
  }

  /**
   * Returns whether the client is connected.
   */
  isConnected(): boolean {
    if (this.client == null) {
      return false;
    }
    return this.client.connected;
  }

  /**
   * Disconnects the websocket.
   */
  disconnect(): void {
    console.log('>> DISCONNECT');
    if (this.client != null) {
      if (this.resultSub != null) {
        this.resultSub.unsubscribe();
      }
      if (this.asSub != null) {
        this.asSub.unsubscribe();
      }
      this.client.deactivate();
      this.client = null;
      this.connectionStateChangedSource.next(false);
      this.initializedStateChangedSource.next(false);
    }
  }

  /**
   * Initializes the websocket.
   *
   * @param url The websocket url.
   * @param language The requested language.
   */
  connect(url: string, language: string): void {
    if (this.client != null) {
      return;
    }

    this.initialData = {
      locale: 'en',
      initialSentence: null
    };
    this.client = new Client({
      brokerURL: url,
      onConnect: receipt => this.onConnected(receipt),
      onStompError: receipt => this.onStompError(receipt),
      onWebSocketError: evt => this.onWebsocketError(evt),
      debug: msg => console.log(msg)
    });

    this.asMessageReceivedSource.next(null); // reset AS panel
    this.resultMessageReceivedSource.next(null); // reset result panel
    this.initializedStateChangedSource.next(true);
  }

  private onConnected(frame: IFrame): void {
    this.resultSub = this.client.subscribe('/user/queue/result', message => this.onResultMessage(message));
    this.asSub = this.client.subscribe('/user/queue/as', message => this.onASMessage(message));
    this.connectionStateChangedSource.next(true);
    this.client.publish({
      destination: '/app/start',
      body: JSON.stringify(this.initialData)
    });
  }

  private onWebsocketError(evt: Event): void {
    console.log('>> ERROR: ' + evt);
  }

  private onStompError(frame: IFrame): void {
    console.log('>> ERROR: ' + frame);
  }

  private onResultMessage(msg: IMessage) {
    console.log('<<< MESSAGE RECEIVED \r\n' + msg.body);
    this.resultMessageReceivedSource.next(msg.body);
  }

  private onASMessage(msg: IMessage) {
    console.log('<<< MESSAGE RECEIVED \r\n' + msg.body);
    this.asMessageReceivedSource.next(msg.body);
  }

}
